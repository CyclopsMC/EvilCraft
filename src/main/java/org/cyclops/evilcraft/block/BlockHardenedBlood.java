package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemHardenedBloodShardConfig;

/**
 * A hardened version of blood.
 * @author rubensworks
 *
 */
public class BlockHardenedBlood extends Block {

    public BlockHardenedBlood(Block.Properties properties) {
        super(properties);
    }

    /*
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return true;
    }
    TODO: loot tables
     */
    
    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, TileEntity tile, ItemStack heldItem){
        // MCP: mineBlockStatArray
        player.addStat(Stats.BLOCK_MINED.get(this), 1);
        player.addExhaustion(0.025F);

        Material material = world.getBlockState(blockPos.add(0, -1, 0)).getMaterial();

        if (material.blocksMovement() || material.isLiquid()) {
            world.setBlockState(blockPos, RegistryEntries.BLOCK_BLOOD.getDefaultState());
        }
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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote() && heldItem != null && heldItem.getItem() == Items.FLINT_AND_STEEL
                && (player.isCreative() || !heldItem.attemptDamageItem(1, world.rand, (ServerPlayerEntity) player))) {
            splitBlock(world, blockPos);
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, blockPos, player, hand, p_225533_6_);
    }

    private void splitBlock(World world, BlockPos blockPos) {
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_HARDENED_BLOOD_SHARD, ItemHardenedBloodShardConfig.minimumDropped
        		+ (int) (Math.random() * (double) ItemHardenedBloodShardConfig.additionalDropped));
        spawnAsEntity(world, blockPos, itemStack);
        world.removeBlock(blockPos, false);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }
}
