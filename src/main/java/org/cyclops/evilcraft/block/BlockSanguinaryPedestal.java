package org.cyclops.evilcraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntitySanguinaryPedestal;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class BlockSanguinaryPedestal extends BlockWithEntity implements IBlockRarityProvider, IBlockTank {

    private final MapCodec<BlockSanguinaryPedestal> codec;
    private final int tier;

    public BlockSanguinaryPedestal(Block.Properties properties, int tier) {
        super(properties, BlockEntitySanguinaryPedestal::new);
        this.tier = tier;
        this.codec = simpleCodec(p -> new BlockSanguinaryPedestal(p, tier));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_SANGUINARY_PEDESTAL.get(), new BlockEntitySanguinaryPedestal.TickerServer());
    }

    public int getTier() {
        return tier;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return tier == 1 ? Rarity.UNCOMMON : Rarity.COMMON;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult p_225533_6_) {
        BlockEntityHelpers.get(worldIn, pos, BlockEntitySanguinaryPedestal.class)
                .ifPresent(tile -> {
                    player.displayClientMessage(Component.literal(String.format(Locale.ROOT, "%,d", tile.getTank().getFluidAmount()))
                            .append(" / ")
                            .append(String.format(Locale.ROOT, "%,d", tile.getTank().getCapacity()))
                            .append(" mB"), true);
                });
        return super.useWithoutItem(state, worldIn, pos, player, p_225533_6_);
    }

    @Override
    public int getDefaultCapacity() {
        return FluidHelpers.BUCKET_VOLUME * BlockEntitySanguinaryPedestal.TANK_BUCKETS;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public ItemStack toggleActivation(ItemStack itemStack, Level world, Player player) {
        return itemStack;
    }

    @Override
    public boolean isActivated(ItemStack itemStack, Item.TooltipContext context) {
        return false;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }
}
