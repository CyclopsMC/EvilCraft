package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.block.multi.DetectionResult;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
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
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false) ? BlockRenderType.ENTITYBLOCK_ANIMATED : super.getRenderType(blockState);
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
    public boolean shouldDisplayFluidOverlay(BlockState blockState, ILightReader world, BlockPos pos, IFluidState fluidState) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(BlockHelpers.getSafeBlockStateProperty(blockState, ACTIVE, false)) {
            if (!TileColossalBloodChest.canWork(world, blockPos)) {
                return ActionResultType.FAIL;
            } else {
                return super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult);
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
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            TileColossalBloodChest tile = TileHelpers.getSafeTile(world, pos, TileColossalBloodChest.class).orElse(null);
            if (tile != null) {
                tile.setSize(Vec3i.NULL_VECTOR);
            }
        }
        triggerDetector(world, pos, true);
    }

    @Override
    public void onBlockAdded(BlockState blockStateNew, World world, BlockPos blockPos, BlockState blockStateOld, boolean isMoving) {
        super.onBlockAdded(blockStateNew, world, blockPos, blockStateOld, isMoving);
        if(!world.captureBlockSnapshots && blockStateNew.getBlock() != blockStateOld.getBlock() && !blockStateNew.get(ACTIVE)) {
            triggerDetector(world, blockPos, true);
        }
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        if(blockState.get(ACTIVE)) triggerDetector(world, blockPos, false);
        super.onPlayerDestroy(world, blockPos, blockState);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if(BlockHelpers.getSafeBlockStateProperty(state, ACTIVE, false)) triggerDetector(world, pos, false);
        // IForgeBlock.super.onBlockExploded(state, world, pos, explosion);
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        getBlock().onExplosionDestroy(world, pos, explosion);
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

    /**
     * Show the structure forming error in the given player chat window.
     * @param world The world.
     * @param blockPos The start position.
     * @param player The player.
     * @param hand The used hand.
     */
    public static void addPlayerChatError(World world, BlockPos blockPos, PlayerEntity player, Hand hand) {
        if(!world.isRemote && player.getHeldItem(hand).isEmpty()) {
            DetectionResult result = TileColossalBloodChest.getCubeDetector().detect(world, blockPos, null, false);
            if (result != null && result.getError() != null) {
                addPlayerChatError(player, result.getError());
            } else {
                player.sendMessage(new TranslationTextComponent("multiblock.evilcraft.colossalbloodchest.error.unexpected"));
            }
        }
    }

    public static void addPlayerChatError(PlayerEntity player, ITextComponent error) {
        ITextComponent chat = new StringTextComponent("");
        ITextComponent prefix = new StringTextComponent("[")
                .appendSibling(new TranslationTextComponent("multiblock.evilcraft.colossalbloodchest.error.prefix"))
                .appendSibling(new StringTextComponent("]: "))
                .setStyle(new Style().
                        setColor(TextFormatting.GRAY).
                        setHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("multiblock.evilcraft.colossalbloodchest.error.prefix.info")
                        ))
                );
        chat.appendSibling(prefix);
        chat.appendSibling(error);
        player.sendMessage(chat);
    }

}
