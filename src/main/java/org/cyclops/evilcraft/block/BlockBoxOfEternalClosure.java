package org.cyclops.evilcraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritData;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

/**
 * A box that can hold beings from higher dimensions.
 * @author rubensworks
 *
 */
public class BlockBoxOfEternalClosure extends BlockWithEntity implements IBlockRarityProvider {

    public static final MapCodec<BlockBoxOfEternalClosure> CODEC = simpleCodec(BlockBoxOfEternalClosure::new);

    public static final String FORGOTTEN_PLAYER = "Forgotten Player";
    private static final int LIGHT_LEVEL = 6;

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public static final VoxelShape SHAPE_EW = Block.box(0.25F * 16F, 0F, 0.0F, 0.75F * 16F, 0.43F * 16F, 1.0F * 16F);
    public static final VoxelShape SHAPE_NS = Block.box(0.0F, 0F, 0.25F * 16F, 1.0F * 16F, 0.43F * 16F, 0.75F * 16F);

    public static ItemStack boxOfEternalClosureFilled;

    public BlockBoxOfEternalClosure(Block.Properties properties) {
        super(properties, BlockEntityBoxOfEternalClosure::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_BOX_OF_ETERNAL_CLOSURE.get(), level.isClientSide ? new BlockEntityBoxOfEternalClosure.TickerClient() : new BlockEntityBoxOfEternalClosure.TickerServer());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction rotation = state.getValue(FACING);
        return rotation == Direction.EAST || rotation == Direction.WEST ? SHAPE_EW : SHAPE_NS;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public static EntityType<?> getSpiritTypeWithFallbackSpirit(ItemStack itemStack) {
        if(itemStack.has(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA)) {
            if (hasPlayer(itemStack)) {
                return EntityType.ZOMBIE;
            }
            EntityType<?> spiritType = getSpiritTypeRaw(itemStack);
            if (spiritType == null && itemStack.has(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA)) {
                return RegistryEntries.ENTITY_VENGEANCE_SPIRIT.get();
            }
            return spiritType;
        }
        return null;
    }

    @Nullable
    public static EntityType<?> getSpiritTypeRaw(ItemStack itemStack) {
        return BlockEntityBoxOfEternalClosure.getSpiritType(itemStack);
    }

    /**
     * Put a vengeance swarm inside the given box.
     * @param itemStack The box.
     */
    public static void setVengeanceSwarmContent(ItemStack itemStack) {
        CompoundTag spiritTag = new CompoundTag();

        EntityVengeanceSpiritData spiritData = new EntityVengeanceSpiritData();
        spiritData.setSwarm(true);
        spiritData.setRandomSwarmTier(new Random());
        spiritData.writeNBT(spiritTag);

        itemStack.set(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, spiritTag);
    }

    /**
     * Put a player inside the given box.
     * @param itemStack The box.
     * @param playerId The player id to set.
     */
    public static void setPlayerContent(ItemStack itemStack, UUID playerId, String name) {
        CompoundTag spiritTag = new CompoundTag();

        EntityVengeanceSpiritData spiritData = new EntityVengeanceSpiritData();
        spiritData.setPlayerId(playerId.toString());
        spiritData.setPlayerName(name);
        spiritData.writeNBT(spiritTag);

        itemStack.set(RegistryEntries.COMPONENT_BOX_PLAYER_ID, spiritData.getPlayerId());
        itemStack.set(RegistryEntries.COMPONENT_BOX_PLAYER_NAME, spiritData.getPlayerName());
        itemStack.set(RegistryEntries.COMPONENT_BOX_SPIRIT_DATA, spiritTag);
    }

    public static String getPlayerName(ItemStack itemStack) {
        return itemStack.getOrDefault(RegistryEntries.COMPONENT_BOX_PLAYER_NAME, "");
    }

    public static String getPlayerId(ItemStack itemStack) {
        return itemStack.getOrDefault(RegistryEntries.COMPONENT_BOX_PLAYER_ID, "");
    }

    public static boolean hasPlayer(ItemStack itemStack) {
        return !getPlayerId(itemStack).isEmpty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return BlockHelpers.doesBlockHaveSolidTopSurface(worldIn, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        if (!worldIn.isAreaLoaded(pos, 1))
            return;
        if (!state.isFaceSturdy(worldIn, pos.below(), Direction.UP)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.isFaceSturdy(worldIn, currentPos.below(), Direction.UP)) {
            worldIn.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult blockRayTraceResult) {
        return BlockEntityHelpers.get(worldIn, pos, BlockEntityBoxOfEternalClosure.class)
                .map(tile -> {
                    if (tile.isClosed()) {
                        tile.open();
                        return InteractionResult.SUCCESS;
                    }
                    return super.useWithoutItem(state, worldIn, pos, player, blockRayTraceResult);
                })
                .orElseGet(() -> super.useWithoutItem(state, worldIn, pos, player, blockRayTraceResult));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return BlockEntityHelpers.get(world, pos, BlockEntityBoxOfEternalClosure.class)
                .map(tile -> tile.getLidAngle() > 0 ? LIGHT_LEVEL : super.getLightEmission(state, world, pos))
                .orElse(0);
    }

    public void fillItemCategory(NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
        items.add(org.cyclops.evilcraft.block.BlockBoxOfEternalClosure.boxOfEternalClosureFilled);
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return hasPlayer(itemStack) ? Rarity.RARE : Rarity.UNCOMMON;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos blockPos) {
        if(world.getBlockEntity(blockPos) != null) {
            BlockEntityBoxOfEternalClosure tile = (BlockEntityBoxOfEternalClosure) world.getBlockEntity(blockPos);
            if(tile.hasSpirit()) {
                return 15;
            }
        }
        return super.getAnalogOutputSignal(blockState, world, blockPos);
    }
}
