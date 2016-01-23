package org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;

import java.util.List;

/**
 * Information on how to handle the Blood Infuser.
 * @author rubensworks
 */
public class SanguinaryEnvironmentalAccumulatorRecipeTransferInfo implements IRecipeTransferInfo {
    @Override
    public Class<? extends Container> getContainerClass() {
        return ContainerSanguinaryEnvironmentalAccumulator.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return SanguinaryEnvironmentalAccumulatorRecipeHandler.CATEGORY;
    }

    @Override
    public List<Slot> getRecipeSlots(Container container) {
        List<Slot> slots = Lists.newLinkedList();
        slots.add(container.getSlot(TileSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE));
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(Container container) {
        List<Slot> slots = Lists.newLinkedList();
        for(int i = 4 + TileSanguinaryEnvironmentalAccumulator.SLOTS; i < container.getInventory().size(); i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }
}
