package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.tileentity.TileSanguinaryPedestal;

import java.util.List;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class BlockSanguinaryPedestal extends BlockTile implements IBlockRarityProvider {

    private final int tier;

    public BlockSanguinaryPedestal(Block.Properties properties, int tier) {
        super(properties, TileSanguinaryPedestal::new);
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return tier == 1 ? Rarity.UNCOMMON : Rarity.COMMON;
    }
}
