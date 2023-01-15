package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A blockState for the poison fluid.
 * @author rubensworks
 *
 */
public class BlockFluidPoison extends LiquidBlock {


    private static final int POISON_DURATION = 5;

    public BlockFluidPoison(Block.Properties builder) {
        super(() -> RegistryEntries.FLUID_POISON, builder);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity && WorldHelpers.efficientTick(world, (POISON_DURATION / 2) * 20)) {
            ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION * 20, 1));
        }
        super.entityInside(state, world, pos, entity);
    }
}
