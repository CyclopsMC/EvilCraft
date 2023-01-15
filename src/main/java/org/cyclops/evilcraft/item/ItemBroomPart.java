package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public String getDescriptionId(ItemStack itemStack) {
        IBroomPart part = getPart(itemStack);
        if(part != null) {
            return part.getTranslationKey();
        }
        return super.getDescriptionId(itemStack);
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
    public boolean isFoil(ItemStack stack) {
        IBroomPart part = getPart(stack);
        if(part != null) {
            return part.isEffect();
        }
        return super.isFoil(stack);
    }

    public void fillItemCategory(NonNullList<ItemStack> list) {
        for (IBroomPart part : BroomParts.REGISTRY.getParts()) {
            for (ItemStack itemStack : BroomParts.REGISTRY.getItemsFromPart(part)) {
                if (itemStack.getItem() == this) {
                    list.add(itemStack);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
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
