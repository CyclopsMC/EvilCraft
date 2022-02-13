package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.List;

/**
 * A hardened version of blood.
 * @author rubensworks
 *
 */
public class BlockHardenedBlood extends Block {

    public BlockHardenedBlood(Block.Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (drops.isEmpty()) {
            ServerWorld world = builder.getLevel();
            BlockPos blockPos = new BlockPos(builder.getOptionalParameter(LootParameters.ORIGIN));
            Material material = world.getBlockState(blockPos.offset(0, -1, 0)).getMaterial();

            if (material.blocksMotion() || material.isLiquid()) {
                world.setBlockAndUpdate(blockPos, RegistryEntries.BLOCK_BLOOD.defaultBlockState());
            }
        }
        return drops;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.NORMAL;
    }
    
    @Override
    public void handleRain(World world, BlockPos blockPos) {
        world.setBlockAndUpdate(blockPos, RegistryEntries.BLOCK_BLOOD.defaultBlockState());
    }
}
