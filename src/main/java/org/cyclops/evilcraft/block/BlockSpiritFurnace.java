package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Explosion;
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

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(ACTIVE, false);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!TileSpiritFurnace.canWork(world, blockPos)) {
            return ActionResultType.FAIL;
        }
        return super.use(blockState, world, blockPos, player, hand, rayTraceResult);
    }
    
    private void triggerDetector(IWorld world, BlockPos blockPos, boolean valid) {
        TileSpiritFurnace.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        triggerDetector(world, pos, true);
    }

    @Override
    public void onPlace(BlockState blockStateNew, World world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onPlace(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock() && !blockStateNew.getValue(ACTIVE)) {
            triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void destroy(IWorld worldIn, BlockPos pos, BlockState state) {
        super.destroy(worldIn, pos, state);
        if(state.getValue(ACTIVE)) triggerDetector(worldIn, pos, false);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if(world.getBlockState(pos).getValue(ACTIVE)) triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        getBlock().wasExploded(world, pos, explosion);
    }

	@Override
	public void onDetect(IWorldReader world, BlockPos location, Vector3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            boolean change = !world.getBlockState(location).getValue(ACTIVE);
            ((World) world).setBlock(location, world.getBlockState(location).setValue(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            TileEntity tile = world.getBlockEntity(location);
            if(tile != null) {
                ((TileSpiritFurnace) tile).setSize(valid ? size : Vector3i.ZERO);
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
