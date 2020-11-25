package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.algorithm.RegionIterator;
import org.cyclops.evilcraft.tileentity.TileSpiritPortal;

/**
 * Portal for throwing in your book and stuff.
 * @author rubensworks
 *
 */
public class BlockSpiritPortal extends BlockTile {

    public static final VoxelShape SHAPE = Block.makeCuboidShape(0.4F * 16F, 0.4F * 16F, 0.4F * 16F, 0.6F * 16F, 0.6F * 16F, 0.6F * 16F);

	public BlockSpiritPortal(Block.Properties properties) {
		super(properties, TileSpiritPortal::new);
        MinecraftForge.EVENT_BUS.register(this);
	}

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    protected static boolean canReplaceBlock(BlockState blockState, IWorldReader world, BlockPos pos) {
        return blockState != null && (blockState.getBlock().isAir(blockState, world, pos)|| blockState.getMaterial().isReplaceable());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void palingDeath(LivingDeathEvent event) {
        if(event.getSource() == ExtendedDamageSource.paling) {
            tryPlacePortal(event.getEntityLiving().world, event.getEntityLiving().getPosition().add(0, 1, 0));
        }
    }

    public boolean tryPlacePortal(World world, BlockPos blockPos) {
        int attempts = 9;
        for(RegionIterator it = new RegionIterator(blockPos, 1, true); it.hasNext() && attempts >= 0;) {
            BlockPos location = it.next();
            if(canReplaceBlock(world.getBlockState(location), world, blockPos)) {
                world.setBlockState(location, RegistryEntries.BLOCK_SPIRIT_PORTAL.getDefaultState(),
                        MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                return true;
            }
            attempts--;
        }
        return false;
    }
}
