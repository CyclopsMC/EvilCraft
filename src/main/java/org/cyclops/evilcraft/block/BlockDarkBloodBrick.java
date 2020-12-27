package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import javax.annotation.Nullable;

/**
 * Wall brick for the Spirit Furnace.
 * @author rubensworks
 *
 */
public class BlockDarkBloodBrick extends Block implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockDarkBloodBrick(Block.Properties properties) {
        super(properties);

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(ACTIVE, false);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }
    
    private void triggerDetector(IWorldReader world, BlockPos blockPos, boolean valid) {
        TileSpiritFurnace.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if(!world.isRemote() && !world.captureBlockSnapshots) {
            triggerDetector(world, pos, true);
        }
    }
    
    @Override
    public void onBlockAdded(BlockState blockStateNew, World world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onBlockAdded(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.isRemote() && !world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock()) {
            triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) triggerDetector(world, blockPos, false);
        super.onPlayerDestroy(world, blockPos, blockState);
    }
    
    @Override
    public void onDetect(IWorldReader world, BlockPos location, Vector3i size, boolean valid, BlockPos originCorner) {
		Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            boolean change = !BlockHelpers.getSafeBlockStateProperty(world.getBlockState(location), ACTIVE, false);
            ((World) world).setBlockState(location, world.getBlockState(location).with(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            if(change) {
                TileSpiritFurnace.detectStructure(world, location, size, valid, originCorner);
            }
		}
	}

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) {
            final Wrapper<BlockPos> tileLocationWrapper = new Wrapper<BlockPos>();
            TileSpiritFurnace.getCubeDetector().detect(world, blockPos, null, (location, blockState1) -> {
                if(blockState1.getBlock() instanceof BlockSpiritFurnace) {
                    tileLocationWrapper.set(location);
                }
                return null;
            }, false);
            BlockPos tileLocation = tileLocationWrapper.get();
            if(tileLocation != null) {
                return world.getBlockState(tileLocation).getBlock().onBlockActivated(blockState, world, tileLocation, player, hand, rayTraceResult);
            }
        }
        return super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult);
    }

}
