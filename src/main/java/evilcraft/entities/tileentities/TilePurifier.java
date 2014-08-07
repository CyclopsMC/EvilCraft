package evilcraft.entities.tileentities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.configurable.ConfigurableEnchantment;
import evilcraft.api.entities.tileentitites.NBTPersist;
import evilcraft.api.entities.tileentitites.TankInventoryTileEntity;
import evilcraft.api.fluids.BloodFluidConverter;
import evilcraft.api.fluids.ImplicitFluidConversionTank;
import evilcraft.api.fluids.SingleUseTank;
import evilcraft.blocks.Purifier;
import evilcraft.blocks.PurifierConfig;
import evilcraft.entities.tileentities.tickaction.bloodchest.DamageableItemRepairAction;
import evilcraft.fluids.Blood;
import evilcraft.items.Blook;
import evilcraft.render.particle.EntityBloodBubbleFX;
import evilcraft.render.particle.EntityMagicFinishFX;

/**
 * Tile for the {@link Purifier}..
 * @author rubensworks
 *
 */
public class TilePurifier extends TankInventoryTileEntity {
    
    private static final int PURIFY_DURATION = 60;
    
    /**
     * The amount of slots.
     */
    public static final int SLOTS = 2;
    /**
     * The purify item slot.
     */
    public static final int SLOT_PURIFY = 0;
    /**
     * The book slot.
     */
    public static final int SLOT_BOOK = 1;
    
    /**
     * Duration in ticks to show the 'poof' animation.
     */
    private static final int ANIMATION_FINISHED_DURATION = 2;
    
    @NBTPersist
    private Float randomRotation = 0F;
    
    private int tick = 0;
    
    /**
     * The allowed book class.
     */
    public static final Class<Blook> ALLOWED_BOOK = Blook.class;
    
    /**
     * The fluid it uses.
     */
    public static final Fluid FLUID = Blood.getInstance();
    
    private static final int MAX_BUCKETS = 3;
    
    /**
     * Book bounce tick count.
     */
    @NBTPersist
    public Integer tickCount = 0;
    /**
     * The next book rotation.
     */
    @NBTPersist
    public Float bookRotation2 = 0F;
    /**
     * The previous book rotation.
     */
    @NBTPersist
    public Float bookRotationPrev = 0F;
    /**
     * The book rotation.
     */
    @NBTPersist
    public Float bookRotation = 0F;
    
    @NBTPersist
    private Integer finishedAnimation = 0;
    
    /**
     * Make a new instance.
     */
    public TilePurifier() {
        super(SLOTS, PurifierConfig._instance.NAMEDID, 1, FluidContainerRegistry.BUCKET_VOLUME * MAX_BUCKETS, PurifierConfig._instance.NAMEDID + "tank", FLUID);
        
        List<Integer> slots = new LinkedList<Integer>();
        slots.add(SLOT_BOOK);
        slots.add(SLOT_PURIFY);
        for(ForgeDirection direction : Helpers.DIRECTIONS)
            addSlotsToSide(direction, slots);
        
        this.setSendUpdateOnInventoryChanged(true);
        this.setSendUpdateOnTankChanged(true);
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }
    
    @Override
    public void updateEntity() {
        int buckets = getBucketsFloored();
        if(getPurifyItem() != null && buckets > 0) {
            tick++;
            boolean done = false;
            
            // Try removing bad enchants.
            for(ConfigurableEnchantment enchant : DamageableItemRepairAction.BAD_ENCHANTS) {
                if(!done) {
                    int enchantmentListID = Helpers.doesEnchantApply(getPurifyItem(), enchant.effectId);
                    if(enchantmentListID > -1) {
                        if(tick >= PURIFY_DURATION) {
                            if(!worldObj.isRemote) {
                                int level = Helpers.getEnchantmentLevel(getPurifyItem(), enchantmentListID);
                                Helpers.setEnchantmentLevel(getPurifyItem(), enchantmentListID, level - 1);
                            }
                            setBuckets(buckets - 1, getBucketsRest());
                            finishedAnimation = ANIMATION_FINISHED_DURATION;
                        }
                        if(worldObj.isRemote)
                            showEffect();
                        done = true;
                    }
                }
            }
            
            // If no bad enchants were found/removed, try disenchanting.
            if(!done && buckets == getMaxBuckets()
                    && getBookItem() != null && getBookItem().getItem().getClass() == ALLOWED_BOOK) {
                NBTTagList enchantmentList = getPurifyItem().getEnchantmentTagList();
                if(enchantmentList != null && enchantmentList.tagCount() > 0) {
                    if(tick >= PURIFY_DURATION) {
                        if(!worldObj.isRemote) {
                            // Init enchantment data.
                            int enchantmentListID = worldObj.rand.nextInt(enchantmentList.tagCount());
                            int level = Helpers.getEnchantmentLevel(getPurifyItem(), enchantmentListID);
                            int id = Helpers.getEnchantmentID(getPurifyItem(), enchantmentListID);
                            ItemStack enchantedItem = new ItemStack(Items.enchanted_book, 1);
                            
                            // Set the enchantment book.
                            Map<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
                            enchantments.put(id, level);
                            EnchantmentHelper.setEnchantments(enchantments, enchantedItem);
                            
                            // Define the enchanted book level.
                            Helpers.setEnchantmentLevel(getPurifyItem(), enchantmentListID, 0);
                            
                            // Put the enchanted book in the book slot.
                            setBookItem(enchantedItem);
                        }
                        finishedAnimation = ANIMATION_FINISHED_DURATION;
                        setBuckets(0, getBucketsRest());
                    }
                    if(worldObj.isRemote) {
                        showEffect();
                        showEnchantingEffect();
                    }
                    done = true;
                }
            }
        } else {
            tick = 0;
        }
        
        // Animation tick/display.
        if(finishedAnimation > 0) {
            finishedAnimation--;
            if(worldObj.isRemote) {
                showEnchantedEffect();
            }
        }
        
        updateBook();
        
        if(tick >= PURIFY_DURATION)
            tick = 0;
    }
    
    /**
     * Get the amount of contained buckets.
     * @return The amount of buckets.
     */
    public int getBucketsFloored() {
        return (int) Math.floor(getTank().getFluidAmount() / (double) FluidContainerRegistry.BUCKET_VOLUME);
    }
    
    /**
     * Get the rest of the fluid that can not fit in a bucket.
     * Use this in {@link TilePurifier#setBuckets(int, int)} as rest.
     * @return The rest of the fluid.
     */
    public int getBucketsRest() {
        return getTank().getFluidAmount() % FluidContainerRegistry.BUCKET_VOLUME;
    }
    
    /**
     * Set the amount of contained buckets. This will also change the inner tank.
     * @param buckets The amount of buckets.
     * @param rest The rest of the fluid.
     */
    public void setBuckets(int buckets, int rest) {
        getTank().setFluid(new FluidStack(FLUID, FluidContainerRegistry.BUCKET_VOLUME * buckets + rest));
        sendUpdate();
    }
    
    /**
     * Set the maximum amount of contained buckets.
     * @return The maximum amount of buckets.
     */
    public int getMaxBuckets() {
        return MAX_BUCKETS;
    }
    
    @Override
    public void sendUpdate() {
        super.sendUpdate();
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, getBucketsFloored(), 2);
        //worldObj.setBlock(xCoord, yCoord, zCoord, Purifier.getInstance());
    }
    
    private void updateBook() {
        this.bookRotationPrev = this.bookRotation2;
        
        this.bookRotation += 0.02F;
        
        while (this.bookRotation2 >= (float)Math.PI) {
            this.bookRotation2 -= ((float)Math.PI * 2F);
        }

        while (this.bookRotation2 < -(float)Math.PI) {
            this.bookRotation2 += ((float)Math.PI * 2F);
        }

        while (this.bookRotation >= (float)Math.PI) {
            this.bookRotation -= ((float)Math.PI * 2F);
        }

        while (this.bookRotation < -(float)Math.PI) {
            this.bookRotation += ((float)Math.PI * 2F);
        }

        float baseNextRotation;

        for (baseNextRotation = this.bookRotation - this.bookRotation2; baseNextRotation >= (float)Math.PI; baseNextRotation -= ((float)Math.PI * 2F)) { }

        while (baseNextRotation < -(float)Math.PI) {
            baseNextRotation += ((float)Math.PI * 2F);
        }

        this.bookRotation2 += baseNextRotation * 0.4F;

        ++this.tickCount;
    }
    
    /**
     * Get the purify item.
     * @return The purify item.
     */
    public ItemStack getPurifyItem() {
        return getStackInSlot(SLOT_PURIFY);
    }
    
    /**
     * Set the purify item.
     * @param itemStack The purify item.
     */
    public void setPurifyItem(ItemStack itemStack) {
        this.randomRotation = worldObj.rand.nextFloat() * 360;
        setInventorySlotContents(SLOT_PURIFY, itemStack);
    }
    
    /**
     * Get the book item.
     * @return The book item.
     */
    public ItemStack getBookItem() {
        return getStackInSlot(SLOT_BOOK);
    }
    
    /**
     * Set the book item.
     * @param itemStack The book item.
     */
    public void setBookItem(ItemStack itemStack) {
        setInventorySlotContents(SLOT_BOOK, itemStack);
    }
    
    @SideOnly(Side.CLIENT)
    private void showEffect() {
        for (int i=0; i < 1; i++) {                
            double particleX = xCoord + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleY = yCoord + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleZ = zCoord + 0.2 + worldObj.rand.nextDouble() * 0.6;

            float particleMotionX = -0.01F + worldObj.rand.nextFloat() * 0.02F;
            float particleMotionY = 0.01F;
            float particleMotionZ = -0.01F + worldObj.rand.nextFloat() * 0.02F;

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                    new EntityBloodBubbleFX(worldObj, particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ)
                    );
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void showEnchantingEffect() {
        if(worldObj.rand.nextInt(10) == 0) {
            for (int i=0; i < 1; i++) {                
                double particleX = xCoord + 0.45 + worldObj.rand.nextDouble() * 0.1;
                double particleY = yCoord + 1.45 + worldObj.rand.nextDouble() * 0.1;
                double particleZ = zCoord + 0.45 + worldObj.rand.nextDouble() * 0.1;
                
                float particleMotionX = -0.4F + worldObj.rand.nextFloat() * 0.8F;
                float particleMotionY = -worldObj.rand.nextFloat();
                float particleMotionZ = -0.4F + worldObj.rand.nextFloat() * 0.8F;
    
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityEnchantmentTableParticleFX(worldObj, particleX, particleY, particleZ,
                                particleMotionX, particleMotionY, particleMotionZ)
                        );
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void showEnchantedEffect() {
        for (int i=0; i < 100; i++) {                
            double particleX = xCoord + 0.45 + worldObj.rand.nextDouble() * 0.1;
            double particleY = yCoord + 1.45 + worldObj.rand.nextDouble() * 0.1;
            double particleZ = zCoord + 0.45 + worldObj.rand.nextDouble() * 0.1;
            
            float particleMotionX = -0.4F + worldObj.rand.nextFloat() * 0.8F;
            float particleMotionY = -0.4F + worldObj.rand.nextFloat() * 0.8F;
            float particleMotionZ = -0.4F + worldObj.rand.nextFloat() * 0.8F;

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                    new EntityMagicFinishFX(worldObj, particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ)
                    );
        }
    }

    /**
     * Get the random rotation for displaying the item.
     * @return The random rotation.
     */
    public float getRandomRotation() {
        return randomRotation;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if(i == 0) {
            return itemStack.stackSize == 1;
        } else if(i == 1) {
            return itemStack.stackSize == 1 && itemStack.getItem().getClass() == ALLOWED_BOOK;
        }
        return false;
    }

}
