package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.tileentity.TileDarkTank;
import org.cyclops.evilcraft.tileentity.TileSanguinaryPedestal;

import java.util.Locale;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class BlockSanguinaryPedestal extends BlockTile implements IBlockRarityProvider, IBlockTank {

    private final int tier;

    public BlockSanguinaryPedestal(Block.Properties properties, int tier) {
        super(properties, TileSanguinaryPedestal::new);
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return tier == 1 ? Rarity.UNCOMMON : Rarity.COMMON;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        TileHelpers.getSafeTile(worldIn, pos, TileSanguinaryPedestal.class)
                .ifPresent(tile -> {
                    player.displayClientMessage(new StringTextComponent(String.format(Locale.ROOT, "%,d", tile.getTank().getFluidAmount()))
                            .append(" / ")
                            .append(String.format(Locale.ROOT, "%,d", tile.getTank().getCapacity()))
                            .append(" mB"), true);
                });
        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    @Override
    public int getDefaultCapacity() {
        return FluidHelpers.BUCKET_VOLUME * TileSanguinaryPedestal.TANK_BUCKETS;
    }

    @Override
    public boolean isActivatable() {
        return false;
    }

    @Override
    public ItemStack toggleActivation(ItemStack itemStack, World world, PlayerEntity player) {
        return itemStack;
    }

    @Override
    public boolean isActivated(ItemStack itemStack, World world) {
        return false;
    }
}
