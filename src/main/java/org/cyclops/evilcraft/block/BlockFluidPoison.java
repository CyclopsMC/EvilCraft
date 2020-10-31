package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * A blockState for the poison fluid.
 * @author rubensworks
 *
 */
public class BlockFluidPoison extends FlowingFluidBlock {

    
    private static final int POISON_DURATION = 5;

    public BlockFluidPoison(Block.Properties builder) {
        super(() -> RegistryEntries.FLUID_POISON, builder);
    }
    
    @Override
    public void onEntityWalk(World world, BlockPos blockPos, Entity entity) {
        if(entity instanceof LivingEntity && WorldHelpers.efficientTick(world, (POISON_DURATION / 2) * 20)) {
            ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.POISON, POISON_DURATION * 20, 1));
        }
        super.onEntityWalk(world, blockPos, entity);
    }

}
