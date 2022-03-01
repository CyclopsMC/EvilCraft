package org.cyclops.evilcraft.blockentity;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierActionRegistry;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTankInventory;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;

/**
 * Tile for the {@link org.cyclops.evilcraft.block.BlockPurifier}..
 * @author rubensworks
 *
 */
public class BlockEntityPurifier extends BlockEntityTankInventory {

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

    @NBTPersist
    private Float randomRotation = 0F;

    @Getter
    private int tick = 0;

    public static final int MAX_BUCKETS = 3;

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

    /* Copied from EnchantingTableTileEntity */
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;

    /**
     * Make a new instance.
     */
    public BlockEntityPurifier(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_PURIFIER, blockPos, blockState, SLOTS, 1, FluidHelpers.BUCKET_VOLUME * MAX_BUCKETS, RegistryEntries.FLUID_BLOOD);

        // Trigger render update client-side
        getInventory().addDirtyMarkListener(this::sendUpdate);
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize) {
            @Override
            public boolean canPlaceItem(int i, ItemStack itemStack) {
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
     * Use this in {@link BlockEntityPurifier#setBuckets(int, int)} as rest.
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

        /* Copied from EnchantingTableTileEntity */
        float f2;
        for(f2 = this.tRot - this.rot; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
            ;
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }

        this.rot += f2 * 0.4F;
        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        ++this.time;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        float f3 = 0.2F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.flipA += (f - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

    /**
     * Get the purify item.
     * @return The purify item.
     */
    public ItemStack getPurifyItem() {
        return getInventory().getItem(SLOT_PURIFY);
    }

    /**
     * Set the purify item.
     * @param itemStack The purify item.
     */
    public void setPurifyItem(ItemStack itemStack) {
        this.randomRotation = level.random.nextFloat() * 360;
        getInventory().setItem(SLOT_PURIFY, itemStack);
    }

    /**
     * Get the book item.
     * @return The book item.
     */
    public ItemStack getAdditionalItem() {
        return getInventory().getItem(SLOT_ADDITIONAL);
    }

    /**
     * Set the book item.
     * @param itemStack The book item.
     */
    public void setAdditionalItem(ItemStack itemStack) {
        getInventory().setItem(SLOT_ADDITIONAL, itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    public void showEffect() {
        for (int i=0; i < 1; i++) {
            double particleX = getBlockPos().getX() + 0.2 + level.random.nextDouble() * 0.6;
            double particleY = getBlockPos().getY() + 0.2 + level.random.nextDouble() * 0.6;
            double particleZ = getBlockPos().getZ() + 0.2 + level.random.nextDouble() * 0.6;

            float particleMotionX = -0.01F + level.random.nextFloat() * 0.02F;
            float particleMotionY = 0.01F;
            float particleMotionZ = -0.01F + level.random.nextFloat() * 0.02F;

            Minecraft.getInstance().levelRenderer.addParticle(
                    RegistryEntries.PARTICLE_BLOOD_BUBBLE, false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void showEnchantingEffect() {
        if(level.random.nextInt(10) == 0) {
            for (int i=0; i < 1; i++) {
                double particleX = getBlockPos().getX() + 0.45 + level.random.nextDouble() * 0.1;
                double particleY = getBlockPos().getY() + 1.45 + level.random.nextDouble() * 0.1;
                double particleZ = getBlockPos().getZ() + 0.45 + level.random.nextDouble() * 0.1;

                float particleMotionX = -0.4F + level.random.nextFloat() * 0.8F;
                float particleMotionY = -level.random.nextFloat();
                float particleMotionZ = -0.4F + level.random.nextFloat() * 0.8F;

                Minecraft.getInstance().levelRenderer.addParticle(
                        ParticleTypes.ENCHANT, false,
                        particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void showEnchantedEffect() {
        for (int i=0; i < 100; i++) {
            double particleX = getBlockPos().getX() + 0.45 + level.random.nextDouble() * 0.1;
            double particleY = getBlockPos().getY() + 1.45 + level.random.nextDouble() * 0.1;
            double particleZ = getBlockPos().getZ() + 0.45 + level.random.nextDouble() * 0.1;

            float particleMotionX = -0.4F + level.random.nextFloat() * 0.8F;
            float particleMotionY = -0.4F + level.random.nextFloat() * 0.8F;
            float particleMotionZ = -0.4F + level.random.nextFloat() * 0.8F;

            Minecraft.getInstance().levelRenderer.addParticle(
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

    @Override
    public void onTankChanged() {
        super.onTankChanged();
        sendUpdate();
    }

    public static class Ticker extends BlockEntityTickerDelayed<BlockEntityPurifier> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityPurifier blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            int actionId = blockEntity.currentAction;
            if (actionId < 0) {
                actionId = blockEntity.getActions().canWork(blockEntity);
            }
            if (actionId >= 0) {
                blockEntity.tick++;
                if(blockEntity.getActions().work(actionId, blockEntity)) {
                    blockEntity.tick = 0;
                    blockEntity.currentAction = -1;
                    blockEntity.onActionFinished();
                }
            } else {
                blockEntity.tick = 0;
                blockEntity.currentAction = -1;
            }

            // Animation tick/display.
            if (blockEntity.finishedAnimation > 0) {
                blockEntity.finishedAnimation--;
                if (level.isClientSide()) {
                    blockEntity.showEnchantedEffect();
                }
            }

            blockEntity.updateAdditionalItem();
        }
    }
}
