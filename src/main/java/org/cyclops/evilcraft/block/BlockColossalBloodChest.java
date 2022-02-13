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
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.block.multi.DetectionResult;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

import javax.annotation.Nullable;

/**
 * A machine that can infuse stuff with blood.
 *
 * @author rubensworks
 */
public class BlockColossalBloodChest extends BlockTileGuiTank implements CubeDetector.IDetectionListener {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockColossalBloodChest(Block.Properties properties) {
        super(properties, TileColossalBloodChest::new);

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
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) {
            if (!TileColossalBloodChest.canWork(world, blockPos)) {
                return ActionResultType.FAIL;
            } else {
                return super.use(blockState, world, blockPos, player, hand, rayTraceResult);
            }
        } else {
            addPlayerChatError(world, blockPos, player, hand);
            return ActionResultType.FAIL;
        }
    }

    @Override
    public int getDefaultCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }

    public static void triggerDetector(IWorldReader world, BlockPos blockPos, boolean valid) {
        TileColossalBloodChest.getCubeDetector().detect(world, blockPos, valid ? null : blockPos, true);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (stack.hasCustomHoverName()) {
            TileColossalBloodChest tile = TileHelpers.getSafeTile(world, pos, TileColossalBloodChest.class).orElse(null);
            if (tile != null) {
                tile.setSize(Vector3i.ZERO);
            }
        }
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
    public void destroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(ACTIVE)) triggerDetector(world, blockPos, false);
        super.destroy(world, blockPos, blockState);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        getBlock().wasExploded(world, pos, explosion);
    }

    @Override
    public void onDetect(IWorldReader world, BlockPos location, Vector3i size, boolean valid, BlockPos originCorner) {
        Block block = world.getBlockState(location).getBlock();
        if(block == this) {
            ((World) world).setBlock(location, world.getBlockState(location).setValue(ACTIVE, valid), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            TileHelpers.getSafeTile(world, location, TileColossalBloodChest.class)
                    .ifPresent(tile -> {
                        tile.setSize(valid ? size : Vector3i.ZERO);
                        tile.setCenter(originCorner.offset(1, 1, 1));
                    });
        }
    }

    /**
     * Show the structure forming error in the given player chat window.
     * @param world The world.
     * @param blockPos The start position.
     * @param player The player.
     * @param hand The used hand.
     */
    public static void addPlayerChatError(World world, BlockPos blockPos, PlayerEntity player, Hand hand) {
        if(!world.isClientSide && player.getItemInHand(hand).isEmpty()) {
            DetectionResult result = TileColossalBloodChest.getCubeDetector().detect(world, blockPos, null, false);
            if (result != null && result.getError() != null) {
                addPlayerChatError(player, result.getError());
            } else {
                player.sendMessage(new TranslationTextComponent("multiblock.evilcraft.colossalbloodchest.error.unexpected"), Util.NIL_UUID);
            }
        }
    }

    public static void addPlayerChatError(PlayerEntity player, ITextComponent error) {
        IFormattableTextComponent chat = new StringTextComponent("");
        IFormattableTextComponent prefix = new StringTextComponent("[")
                .append(new TranslationTextComponent("multiblock.evilcraft.colossalbloodchest.error.prefix"))
                .append(new StringTextComponent("]: "))
                .setStyle(Style.EMPTY.
                        applyFormat(TextFormatting.GRAY).
                        withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("multiblock.evilcraft.colossalbloodchest.error.prefix.info")
                        ))
                );
        chat.append(prefix);
        chat.append(error);
        player.sendMessage(chat, Util.NIL_UUID);
    }

}
