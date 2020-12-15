package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
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
            ServerWorld world = builder.getWorld();
            BlockPos blockPos = builder.get(LootParameters.POSITION);
            Material material = world.getBlockState(blockPos.add(0, -1, 0)).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                world.setBlockState(blockPos, RegistryEntries.BLOCK_BLOOD.getDefaultState());
            }
        }
        return drops;
    }

    @Override
    public PushReaction getPushReaction(BlockState blockState) {
        return PushReaction.NORMAL;
    }
    
    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        world.setBlockState(blockPos, RegistryEntries.BLOCK_BLOOD.getDefaultState());
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }
}
