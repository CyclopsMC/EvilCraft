package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEternalWater;

import javax.annotation.Nullable;

/**
 * Block for {@link BlockEternalWaterConfig}.
 * @author rubensworks
 */
public class BlockEternalWater extends BlockWithEntity {

    public BlockEternalWater(Block.Properties properties) {
        super(properties, BlockEntityEternalWater::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_ETERNAL_WATER, new BlockEntityEternalWater.TickerServer());
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos blockPos, Player player,
                                             InteractionHand hand, BlockHitResult p_225533_6_) {
        ItemStack itemStack = player.getInventory().getSelected();
        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() == Items.BUCKET) {
                if (!world.isClientSide()) {
                    itemStack.shrink(1);
                    if (itemStack.isEmpty()) {
                        player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
                    } else if (!player.getInventory().add(new ItemStack(Items.WATER_BUCKET))) {
                        player.drop(new ItemStack(Items.WATER_BUCKET), false);
                    }
                    world.playSound(null, blockPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                FluidUtil.getFluidHandler(itemStack)
                        .ifPresent(fluidHandler -> fluidHandler.fill(BlockEntityEternalWater.WATER, IFluidHandler.FluidAction.EXECUTE));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
        // When removing this block, it will drop water, so forcefully set to air instead.
        if (!worldIn.isClientSide() && newState.getBlock() == Blocks.WATER) {
            worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            Block.dropResources(state, worldIn, pos);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void stopFillWithEternalWaterBlock(FillBucketEvent event) {
        if (event.getTarget() != null && event.getTarget().getType() == HitResult.Type.BLOCK) {
            Block block = event.getLevel().getBlockState(((BlockHitResult) event.getTarget()).getBlockPos()).getBlock();
            if (block instanceof BlockEternalWater) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.defaultFluidState();
    }
}
