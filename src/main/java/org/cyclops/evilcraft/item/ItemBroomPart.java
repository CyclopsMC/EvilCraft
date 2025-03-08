package org.cyclops.evilcraft.item;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public Component getName(ItemStack stack) {
        IBroomPart part = getPart(stack);
        if(part != null) {
            return Component.translatable(part.getTranslationKey());
        }
        return super.getName(stack);
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
}
