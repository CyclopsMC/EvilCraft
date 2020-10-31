package org.cyclops.evilcraft.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

/**
 * Item-form of a broom part.
 * @author rubensworks
 *
 */
public class ItemBroomPart extends Item {

    public ItemBroomPart(Item.Properties properties) {
        super(properties);
    }

    public static IBroomPart getPart(ItemStack itemStack) {
        return BroomParts.REGISTRY.getPartFromItem(itemStack);
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        IBroomPart part = getPart(itemStack);
        if(part != null) {
            return part.getTranslationKey();
        }
        return super.getTranslationKey(itemStack);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        IBroomPart part = getPart(stack);
        if(part != null) {
            return part.getRarity();
        }
        return super.getRarity(stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        IBroomPart part = getPart(stack);
        if(part != null) {
            return part.isEffect();
        }
        return super.hasEffect(stack);
    }

    @Override
    public void fillItemGroup(ItemGroup creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
        for (IBroomPart part : BroomParts.REGISTRY.getParts()) {
            for (ItemStack itemStack : BroomParts.REGISTRY.getItemsFromPart(part)) {
                if (itemStack.getItem() == this) {
                    list.add(itemStack);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            IBroomPart part = ItemBroomPart.getPart(itemStack);
            if (part != null) {
                return part.getModelColor();
            }
            return -1;
        }
    }
}
