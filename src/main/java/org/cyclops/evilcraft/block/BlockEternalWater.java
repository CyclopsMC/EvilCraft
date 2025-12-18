package org.cyclops.evilcraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityEternalWater;

import javax.annotation.Nullable;
import java.util.Optional;

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
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_ETERNAL_WATER.get(), new BlockEntityEternalWater.TickerServer());
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
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        ItemStack itemStack = pPlayer.getInventory().getSelectedItem();
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
                return InteractionResult.SUCCESS;
            } else {
                Optional.ofNullable(itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack)))
                        .ifPresent(fluidHandler -> {
                            try (var tx = Transaction.openRoot()) {
                                fluidHandler.insert(FluidResource.of(BlockEntityEternalWater.WATER), BlockEntityEternalWater.WATER.getAmount(), tx);
                                tx.commit();
                            }
                        });
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.defaultFluidState();
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        // When removing this block, it will drop water, so forcefully set to air instead.
        if (!level.isClientSide()) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), IModHelpers.get().getMinecraftHelpers().getBlockNotifyClient());
        }
    }
}
