package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.cyclops.evilcraft.tileentity.TileBoxOfEternalClosure;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockBoxOfEternalClosure extends ItemBlockNBT {

    public ItemBlockBoxOfEternalClosure(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
        if (tile instanceof TileBoxOfEternalClosure && itemStack.hasTag()) {
            ((TileBoxOfEternalClosure) tile).setSpiritTag(itemStack.getTag().getCompound(TileBoxOfEternalClosure.NBTKEY_SPIRIT));
            ((TileBoxOfEternalClosure) tile).setPlayerId(itemStack.getTag().getString(TileBoxOfEternalClosure.NBTKEY_PLAYERID));
            ((TileBoxOfEternalClosure) tile).setPlayerName(itemStack.getTag().getString(TileBoxOfEternalClosure.NBTKEY_PLAYERNAME));
            ((TileBoxOfEternalClosure) tile).initializeState();
        }
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(getInfo(stack));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    public ITextComponent getInfo(ItemStack itemStack) {
        ITextComponent content = new TranslationTextComponent("general." + Reference.MOD_ID + ".info.empty")
                .mergeStyle(TextFormatting.ITALIC);
        if(BlockBoxOfEternalClosure.hasPlayer(itemStack)) {
            content = new StringTextComponent(BlockBoxOfEternalClosure.getPlayerName(itemStack));
        } else {
            EntityType<?> spiritType = BlockBoxOfEternalClosure.getSpiritTypeWithFallbackSpirit(itemStack);
            if (spiritType != null) {
                content = spiritType.getName();
            }
        }
        return new TranslationTextComponent(getTranslationKey() + ".info.content")
                .mergeStyle(TextFormatting.LIGHT_PURPLE)
                .append(content);
    }
}
