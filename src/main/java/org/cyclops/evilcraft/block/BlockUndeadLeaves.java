package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.component.IEntityDropParticleFXBlock;
import org.cyclops.cyclopscore.block.component.ParticleDropBlockComponent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.Random;

/**
 * Leaves for the Undead Tree.
 * @author rubensworks
 *
 */
public class BlockUndeadLeaves extends LeavesBlock implements IEntityDropParticleFXBlock {
    
    private ParticleDropBlockComponent particleDropBlockComponent;

    public BlockUndeadLeaves(Block.Properties properties) {
        super(properties);
        
        if (MinecraftHelpers.isClientSide()) {
            particleDropBlockComponent = new ParticleDropBlockComponent(1.0F, 0.0F, 0.0F);
            particleDropBlockComponent.setOffset(0);
            particleDropBlockComponent.setChance(50);
        }
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    /*
    @Override
    public Item getItemDropped(BlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(Blocks.DEADBUSH);
    }
    TODO: loot table
     */

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random rand) {
        particleDropBlockComponent.randomDisplayTick(blockState, world, blockPos, rand);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(this);
    }

    /*
    @Override
    protected ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(this);
    }
    TODO: loot tables
     */
}
