package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.algorithm.Wrapper;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

import javax.annotation.Nullable;

/**
 * Part of the Colossal Blood Chest multiblock structure.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlank extends Block implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockReinforcedUndeadPlank(Block.Properties properties) {
        super(properties);

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
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false) ? BlockRenderType.ENTITYBLOCK_ANIMATED : super.getRenderShape(blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos,
                                    EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState blockState, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
        return true;
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if(!world.captureBlockSnapshots) {
            BlockColossalBloodChest.triggerDetector(world, pos, true);
        }
    }

    @Override
    public void onPlace(BlockState blockStateNew, World world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onPlace(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock() && !blockStateNew.getValue(ACTIVE)) {
            BlockColossalBloodChest.triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void destroy(IWorld worldIn, BlockPos pos, BlockState state) {
        super.destroy(worldIn, pos, state);
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) BlockColossalBloodChest.triggerDetector(worldIn, pos, false);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) BlockColossalBloodChest.triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        getBlock().wasExploded(world, pos, explosion);
    }

    @Override
    public void onDetect(IWorldReader world, BlockPos location, Vector3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            boolean change = !BlockHelpers.getSafeBlockStateProperty(world.getBlockState(location), ACTIVE, false);
            ((World) world).setBlock(location, world.getBlockState(location).setValue(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            if(change) {
                TileColossalBloodChest.detectStructure((World) world, location, size, valid, originCorner);
            }
        }
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) {
            final Wrapper<BlockPos> tileLocationWrapper = new Wrapper<BlockPos>();
            TileColossalBloodChest.getCubeDetector().detect(world, blockPos, null, new CubeDetector.IValidationAction() {

                @Override
                public ITextComponent onValidate(BlockPos location, BlockState blockState) {
                    if(blockState.getBlock() instanceof BlockColossalBloodChest) {
                        tileLocationWrapper.set(location);
                    }
                    return null;
                }

            }, false);
            BlockPos tileLocation = tileLocationWrapper.get();
            if(tileLocation != null) {
                return world.getBlockState(tileLocation).getBlock()
                        .use(world.getBlockState(tileLocation), world, tileLocation, player, hand, p_225533_6_);
            }
            return super.use(blockState, world, blockPos, player, hand, p_225533_6_);
        } else {
            BlockColossalBloodChest.addPlayerChatError(world, blockPos, player, hand);
            return ActionResultType.FAIL;
        }
    }

}
