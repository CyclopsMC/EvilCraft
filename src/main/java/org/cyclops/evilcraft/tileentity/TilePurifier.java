package org.cyclops.evilcraft.tileentity;

import lombok.Getter;
import lombok.experimental.Delegate;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierActionRegistry;
import org.cyclops.evilcraft.block.Purifier;
import org.cyclops.evilcraft.block.PurifierConfig;
import org.cyclops.evilcraft.client.particle.EntityBloodBubbleFX;
import org.cyclops.evilcraft.client.particle.EntityMagicFinishFX;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.fluid.Blood;

import java.util.LinkedList;
import java.util.List;

/**
 * Tile for the {@link Purifier}..
 * @author rubensworks
 *
 */
public class TilePurifier extends TankInventoryTileEntity implements CyclopsTileEntity.ITickingTile {
    
    /**
     * The amount of slots.
     */
    public static final int SLOTS = 2;
    /**
     * The purify item slot.
     */
    public static final int SLOT_PURIFY = 0;
    /**
     * The additional slot.
     */
    public static final int SLOT_ADDITIONAL = 1;
    
    /**
     * Duration in ticks to show the 'poof' animation.
     */
    private static final int ANIMATION_FINISHED_DURATION = 2;

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);
    
    @NBTPersist
    private Float randomRotation = 0F;

    @Getter
    private int tick = 0;
    
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
     * The next additional item rotation.
     */
    @NBTPersist
    public Float additionalRotation2 = 0F;
    /**
     * The previous additional item rotation.
     */
    @NBTPersist
    public Float additionalRotationPrev = 0F;
    /**
     * The additional item rotation.
     */
    @NBTPersist
    public Float additionalRotation = 0F;
    
    @NBTPersist
    private Integer finishedAnimation = 0;

    @NBTPersist
    @Getter
    private Integer currentAction = -1;
    
    /**
     * Make a new instance.
     */
    public TilePurifier() {
        super(SLOTS, PurifierConfig._instance.getNamedId(), 1, FluidContainerRegistry.BUCKET_VOLUME * MAX_BUCKETS, PurifierConfig._instance.getNamedId() + "tank", FLUID);
        
        List<Integer> slots = new LinkedList<Integer>();
        slots.add(SLOT_ADDITIONAL);
        slots.add(SLOT_PURIFY);
        for(EnumFacing direction : DirectionHelpers.DIRECTIONS)
            addSlotsToSide(direction, slots);
        
        this.setSendUpdateOnInventoryChanged(true);
        this.setSendUpdateOnTankChanged(true);
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }

    public IPurifierActionRegistry getActions() {
        return EvilCraft._instance.getRegistryManager().getRegistry(IPurifierActionRegistry.class);
    }
    
    @Override
    public void updateTileEntity() {
    	super.updateTileEntity();

        int actionId = currentAction;
        if(actionId < 0) {
            actionId = getActions().canWork(this);
        }
        if(actionId >= 0) {
            tick++;
            if(getActions().work(actionId, this)) {
                tick = 0;
                currentAction = -1;
                onActionFinished();
            }
        } else {
            tick = 0;
            currentAction = -1;
        }
        
        // Animation tick/display.
        if(finishedAnimation > 0) {
            finishedAnimation--;
            if(worldObj.isRemote) {
                showEnchantedEffect();
            }
        }
        
        updateAdditionalItem();
    }

    public void onActionFinished() {
        finishedAnimation = ANIMATION_FINISHED_DURATION;
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
    protected void onSendUpdate() {
        super.onSendUpdate();
        worldObj.setBlockState(getPos(), Purifier.getInstance().getDefaultState().withProperty(Purifier.FILL, getBucketsFloored()), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    }
    
    private void updateAdditionalItem() {
        this.additionalRotationPrev = this.additionalRotation2;
        
        this.additionalRotation += 0.02F;
        
        while (this.additionalRotation2 >= (float)Math.PI) {
            this.additionalRotation2 -= ((float)Math.PI * 2F);
        }

        while (this.additionalRotation2 < -(float)Math.PI) {
            this.additionalRotation2 += ((float)Math.PI * 2F);
        }

        while (this.additionalRotation >= (float)Math.PI) {
            this.additionalRotation -= ((float)Math.PI * 2F);
        }

        while (this.additionalRotation < -(float)Math.PI) {
            this.additionalRotation += ((float)Math.PI * 2F);
        }

        float baseNextRotation;

        for (baseNextRotation = this.additionalRotation - this.additionalRotation2; baseNextRotation >= (float)Math.PI; baseNextRotation -= ((float)Math.PI * 2F)) { }

        while (baseNextRotation < -(float)Math.PI) {
            baseNextRotation += ((float)Math.PI * 2F);
        }

        this.additionalRotation2 += baseNextRotation * 0.4F;

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
    public ItemStack getAdditionalItem() {
        return getStackInSlot(SLOT_ADDITIONAL);
    }
    
    /**
     * Set the book item.
     * @param itemStack The book item.
     */
    public void setAdditionalItem(ItemStack itemStack) {
        setInventorySlotContents(SLOT_ADDITIONAL, itemStack);
    }
    
    @SideOnly(Side.CLIENT)
    public void showEffect() {
        for (int i=0; i < 1; i++) {                
            double particleX = getPos().getX() + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleY = getPos().getY() + 0.2 + worldObj.rand.nextDouble() * 0.6;
            double particleZ = getPos().getZ() + 0.2 + worldObj.rand.nextDouble() * 0.6;

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
    public void showEnchantingEffect() {
        if(worldObj.rand.nextInt(10) == 0) {
            for (int i=0; i < 1; i++) {                
                double particleX = getPos().getX() + 0.45 + worldObj.rand.nextDouble() * 0.1;
                double particleY = getPos().getY() + 1.45 + worldObj.rand.nextDouble() * 0.1;
                double particleZ = getPos().getZ() + 0.45 + worldObj.rand.nextDouble() * 0.1;
                
                float particleMotionX = -0.4F + worldObj.rand.nextFloat() * 0.8F;
                float particleMotionY = -worldObj.rand.nextFloat();
                float particleMotionZ = -0.4F + worldObj.rand.nextFloat() * 0.8F;
    
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityEnchantmentTableParticleFX.EnchantmentTable().getEntityFX(0, worldObj, particleX, particleY, particleZ,
                                particleMotionX, particleMotionY, particleMotionZ)
                        );
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void showEnchantedEffect() {
        for (int i=0; i < 100; i++) {                
            double particleX = getPos().getX() + 0.45 + worldObj.rand.nextDouble() * 0.1;
            double particleY = getPos().getY() + 1.45 + worldObj.rand.nextDouble() * 0.1;
            double particleZ = getPos().getZ() + 0.45 + worldObj.rand.nextDouble() * 0.1;
            
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
            return itemStack.stackSize == 1 && getActions().isItemValidForMainSlot(itemStack);
        } else if(i == 1) {
            return itemStack.stackSize == 1 && getActions().isItemValidForAdditionalSlot(itemStack);
        }
        return false;
    }

}
