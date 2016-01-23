package org.cyclops.evilcraft.modcompat.jei.exaltedcrafter;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;

import java.util.ArrayList;
import java.util.List;

/**
 * Information on how to handle the Exalted Crafter.
 * @author rubensworks
 */
public class ExaltedCrafterRecipeTransferInfo implements IRecipeTransferInfo {
    @Override
    public Class<? extends Container> getContainerClass() {
        return ContainerExaltedCrafter.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    public List<Slot> getRecipeSlots(Container container) {
        List<Slot> slots = new ArrayList<Slot>();
        for(int i = 0; i < 9; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(Container container) {
        List<Slot> slots = new ArrayList<Slot>();
        for(int i = 10; i < container.getInventory().size(); i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }
}
