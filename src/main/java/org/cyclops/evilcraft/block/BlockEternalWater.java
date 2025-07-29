package org.cyclops.evilcraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
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
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEternalWater;

import javax.annotation.Nullable;

/**
 * Block for {@link BlockEternalWaterConfig}.
 * @author rubensworks
 */
public class BlockEternalWater extends BlockWithEntity {

    public static final MapCodec<BlockEternalWater> CODEC = simpleCodec(BlockEternalWater::new);

    public BlockEternalWater(Block.Properties properties) {
        super(properties, BlockEntityEternalWater::new);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_ETERNAL_WATER.get(), new BlockEntityEternalWater.TickerServer());
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
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        ItemStack itemStack = pPlayer.getInventory().getSelected();
        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() == Items.BUCKET) {
                if (!pLevel.isClientSide()) {
                    itemStack.shrink(1);
                    if (itemStack.isEmpty()) {
                        pPlayer.setItemInHand(pHand, new ItemStack(Items.WATER_BUCKET));
                    } else if (!pPlayer.getInventory().add(new ItemStack(Items.WATER_BUCKET))) {
                        pPlayer.drop(new ItemStack(Items.WATER_BUCKET), false);
                    }
                    pLevel.playSound(null, pPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            } else {
                FluidUtil.getFluidHandler(itemStack)
                        .ifPresent(fluidHandler -> fluidHandler.fill(BlockEntityEternalWater.WATER, IFluidHandler.FluidAction.EXECUTE));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
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

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.defaultFluidState();
    }
}
