package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

/**
 * A machine that can infuse stuff with blood.
 *
 * @author rubensworks
 */
public class BlockColossalBloodChest extends BlockTileGuiTank implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockColossalBloodChest(Block.Properties properties) {
        super(properties, TileColossalBloodChest::new);

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
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!TileColossalBloodChest.canWork(world, blockPos)) {
            return ActionResultType.FAIL;
        }
        return super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult);
    }

    @Override
    public int getDefaultCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }

    private void triggerDetector(IWorldReader world, BlockPos blockPos, boolean valid) {
        TileColossalBloodChest.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void onBlockAdded(BlockState blockStateNew, World world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onBlockAdded(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock()) {
            triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        if(blockState.get(ACTIVE)) triggerDetector(world, blockPos, false);
        super.onPlayerDestroy(world, blockPos, blockState);
    }

    @Override
    public void onDetect(IWorldReader world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            ((World) world).setBlockState(location, world.getBlockState(location).with(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            TileHelpers.getSafeTile(world, location, TileColossalBloodChest.class)
                    .ifPresent(tile -> {
                        tile.setSize(valid ? size : Vec3i.NULL_VECTOR);
                        tile.setCenter(originCorner.add(1, 1, 1));
                    });
        }
    }

}
