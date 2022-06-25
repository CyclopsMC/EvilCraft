package org.cyclops.evilcraft.blockentity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.RegistryEntries;


/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class BlockEntitySpiritPortal extends CyclopsBlockEntity {

    @NBTPersist
    private Float progress = 0f;

    public BlockEntitySpiritPortal(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_SPIRIT_PORTAL, blockPos, blockState);
    }

    @OnlyIn(Dist.CLIENT)
    private void showNewBlurParticle() {
        RandomSource rand = level.random;
        float scale = 0.6F - rand.nextFloat() * 0.3F;
        float red = rand.nextFloat() * 0.03F + 0.01F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 6.5D + 10D);

        Minecraft.getInstance().levelRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getBlockPos().getX() + 0.5F, getBlockPos().getY() + 0.5F, getBlockPos().getZ() + 0.5F,
                rand.nextFloat() * 0.2F - 0.1F, rand.nextFloat() * 0.2F - 0.1F, rand.nextFloat() * 0.2F - 0.1F);
    }

    public float getProgress() {
        return progress;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return BlockEntity.INFINITE_EXTENT_AABB;
    }

    public static class Ticker extends BlockEntityTickerDelayed<BlockEntitySpiritPortal> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntitySpiritPortal blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            blockEntity.progress += 0.005f;
            if(blockEntity.progress > 1) {
                level.removeBlock(pos, false);
            }
            if(level.isClientSide()) {
                int progressModifier = (int) (blockEntity.getProgress() * 40f) + 1;
                if(level.random.nextInt(5) == 0) {
                    level.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                            EvilCraftSoundEvents.effect_vengeancebeam_base, SoundSource.AMBIENT,
                            0.5F + level.random.nextFloat() * 0.2F, 1.0F, false);
                }
                for(int i = 0; i < level.random.nextInt(progressModifier); i++) {
                    blockEntity.showNewBlurParticle();
                }
            }

            // transform book if thrown in.
            if(!level.isClientSide()) {
                for (ItemEntity entityItem : level.getEntitiesOfClass(ItemEntity.class,
                        new AABB(
                                pos.getX() - 0.5D, pos.getY() - 0.5D, pos.getZ() - 0.5D,
                                pos.getX() + 1.5D, pos.getY() + 1.5D, pos.getZ() + 1.5D))) {
                    if (entityItem.getItem().getItem() instanceof BookItem) {
                        Entity entity = new ItemEntity(level, entityItem.getX(), entityItem.getY(), entityItem.getZ(),
                                new ItemStack(RegistryEntries.ITEM_ORIGINS_OF_DARKNESS, entityItem.getItem().getCount()));
                        entity.setDeltaMovement(entityItem.getDeltaMovement());
                        entityItem.remove(Entity.RemovalReason.DISCARDED);
                        level.addFreshEntity(entity);
                    }
                }
            }
        }
    }

}
