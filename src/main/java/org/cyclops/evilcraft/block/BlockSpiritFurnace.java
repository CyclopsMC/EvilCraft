package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BlockSpiritFurnace extends BlockTileGuiTank implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockSpiritFurnace(Block.Properties properties) {
        super(properties, TileSpiritFurnace::new);

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
        if (!TileSpiritFurnace.canWork(world, blockPos)) {
            return ActionResultType.FAIL;
        }
        return super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult);
    }
    
    private void triggerDetector(IWorld world, BlockPos blockPos, boolean valid) {
        TileSpiritFurnace.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if(!world.isRemote()) {
            triggerDetector(world, pos, true);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos blockPos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, world, blockPos, oldState, isMoving);
        if(!world.isRemote()) {
            triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        super.onPlayerDestroy(worldIn, pos, state);
        if(state.get(ACTIVE)) triggerDetector(worldIn, pos, false);
    }

	@Override
	public void onDetect(IWorldReader world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            boolean change = !world.getBlockState(location).get(ACTIVE);
            ((World) world).setBlockState(location, world.getBlockState(location).with(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            TileEntity tile = world.getTileEntity(location);
            if(tile != null) {
                ((TileSpiritFurnace) tile).setSize(valid ? size : Vec3i.NULL_VECTOR);
            }
            if(change) {
                TileSpiritFurnace.detectStructure(world, location, size, valid, originCorner);
            }
        }
	}

    @Override
    public int getDefaultCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }
}
