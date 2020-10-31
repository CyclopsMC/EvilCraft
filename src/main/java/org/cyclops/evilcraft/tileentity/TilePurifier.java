package org.cyclops.evilcraft.tileentity;

import lombok.Getter;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierActionRegistry;
import org.cyclops.evilcraft.block.BlockPurifier;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.TankInventoryTileEntity;

/**
 * Tile for the {@link BlockPurifier}..
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
        super(RegistryEntries.TILE_ENTITY_PURIFIER, SLOTS, 1, FluidHelpers.BUCKET_VOLUME * MAX_BUCKETS, RegistryEntries.FLUID_BLOOD);
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize) {
            @Override
            public boolean isItemValidForSlot(int i, ItemStack itemStack) {
                if(i == 0) {
                    return itemStack.getCount() == 1 && getActions().isItemValidForMainSlot(itemStack);
                } else if(i == 1) {
                    return itemStack.getCount() == 1 && getActions().isItemValidForAdditionalSlot(itemStack);
                }
                return false;
            }
        };
    }

    @Override
    protected SingleUseTank createTank(int tankSize) {
    	return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
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
            if(world.isRemote()) {
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
        return (int) Math.floor(getTank().getFluidAmount() / (double) FluidHelpers.BUCKET_VOLUME);
    }
    
    /**
     * Get the rest of the fluid that can not fit in a bucket.
     * Use this in {@link TilePurifier#setBuckets(int, int)} as rest.
     * @return The rest of the fluid.
     */
    public int getBucketsRest() {
        return getTank().getFluidAmount() % FluidHelpers.BUCKET_VOLUME;
    }
    
    /**
     * Set the amount of contained buckets. This will also change the inner tank.
     * @param buckets The amount of buckets.
     * @param rest The rest of the fluid.
     */
    public void setBuckets(int buckets, int rest) {
        getTank().setFluid(new FluidStack(RegistryEntries.FLUID_BLOOD, FluidHelpers.BUCKET_VOLUME * buckets + rest));
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
        world.setBlockState(getPos(), getBlockState().getBlock().getDefaultState().with(BlockPurifier.FILL, getBucketsFloored()), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
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
        return getInventory().getStackInSlot(SLOT_PURIFY);
    }
    
    /**
     * Set the purify item.
     * @param itemStack The purify item.
     */
    public void setPurifyItem(ItemStack itemStack) {
        this.randomRotation = world.rand.nextFloat() * 360;
        getInventory().setInventorySlotContents(SLOT_PURIFY, itemStack);
    }
    
    /**
     * Get the book item.
     * @return The book item.
     */
    public ItemStack getAdditionalItem() {
        return getInventory().getStackInSlot(SLOT_ADDITIONAL);
    }
    
    /**
     * Set the book item.
     * @param itemStack The book item.
     */
    public void setAdditionalItem(ItemStack itemStack) {
        getInventory().setInventorySlotContents(SLOT_ADDITIONAL, itemStack);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void showEffect() {
        for (int i=0; i < 1; i++) {                
            double particleX = getPos().getX() + 0.2 + world.rand.nextDouble() * 0.6;
            double particleY = getPos().getY() + 0.2 + world.rand.nextDouble() * 0.6;
            double particleZ = getPos().getZ() + 0.2 + world.rand.nextDouble() * 0.6;

            float particleMotionX = -0.01F + world.rand.nextFloat() * 0.02F;
            float particleMotionY = 0.01F;
            float particleMotionZ = -0.01F + world.rand.nextFloat() * 0.02F;

            Minecraft.getInstance().worldRenderer.addParticle(
                    RegistryEntries.PARTICLE_BLOOD_BUBBLE, false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void showEnchantingEffect() {
        if(world.rand.nextInt(10) == 0) {
            for (int i=0; i < 1; i++) {                
                double particleX = getPos().getX() + 0.45 + world.rand.nextDouble() * 0.1;
                double particleY = getPos().getY() + 1.45 + world.rand.nextDouble() * 0.1;
                double particleZ = getPos().getZ() + 0.45 + world.rand.nextDouble() * 0.1;
                
                float particleMotionX = -0.4F + world.rand.nextFloat() * 0.8F;
                float particleMotionY = -world.rand.nextFloat();
                float particleMotionZ = -0.4F + world.rand.nextFloat() * 0.8F;

                Minecraft.getInstance().worldRenderer.addParticle(
                        ParticleTypes.ENCHANT, false,
                        particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void showEnchantedEffect() {
        for (int i=0; i < 100; i++) {                
            double particleX = getPos().getX() + 0.45 + world.rand.nextDouble() * 0.1;
            double particleY = getPos().getY() + 1.45 + world.rand.nextDouble() * 0.1;
            double particleZ = getPos().getZ() + 0.45 + world.rand.nextDouble() * 0.1;
            
            float particleMotionX = -0.4F + world.rand.nextFloat() * 0.8F;
            float particleMotionY = -0.4F + world.rand.nextFloat() * 0.8F;
            float particleMotionZ = -0.4F + world.rand.nextFloat() * 0.8F;

            Minecraft.getInstance().worldRenderer.addParticle(
                    RegistryEntries.PARTICLE_MAGIC_FINISH, false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }

    /**
     * Get the random rotation for displaying the item.
     * @return The random rotation.
     */
    public float getRandomRotation() {
        return randomRotation;
    }

}
