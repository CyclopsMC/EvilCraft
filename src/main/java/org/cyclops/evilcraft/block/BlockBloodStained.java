package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.tileentity.TileBloodStained;

/**
 * Multiple blockState types (defined by metadata) that have blood stains.
 * @author rubensworks
 *
 */
public class BlockBloodStained extends BlockTile {

    // TODO: reimplement like redstone dust block

    public BlockBloodStained(Block.Properties properties) {
        super(properties, TileBloodStained::new);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        splash(worldIn, pos);
        super.onBlockClicked(state, worldIn, pos, player);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        splash(worldIn, pos);
        super.onEntityCollision(state, worldIn, pos, entityIn);
    }
    
    /**
     * Spawn particles.
     * @param world The world.
     * @param blockPos The position.
     */
    @OnlyIn(Dist.CLIENT)
    public static void splash(World world, BlockPos blockPos) {
    	if(MinecraftHelpers.isClientSide()) {
    		ParticleBloodSplash.spawnParticles(world, blockPos.add(0, 1, 0), 0, 1 + world.rand.nextInt(3));
    	}
    }
    
    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        world.removeBlock(blockPos, false);
    }

}
