package org.cyclops.evilcraft.modcompat.jei.bloodinfuser;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.cyclops.evilcraft.inventory.container.ContainerBloodInfuser;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

import java.util.List;

/**
 * Information on how to handle the Blood Infuser.
 * @author rubensworks
 */
public class BloodInfuserRecipeTransferInfo implements IRecipeTransferInfo {
    @Override
    public Class<? extends Container> getContainerClass() {
        return ContainerBloodInfuser.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return BloodInfuserRecipeHandler.CATEGORY;
    }

    @Override
    public List<Slot> getRecipeSlots(Container container) {
        List<Slot> slots = Lists.newLinkedList();
        slots.add(container.getSlot(TileBloodInfuser.SLOT_INFUSE));
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(Container container) {
        List<Slot> slots = Lists.newLinkedList();
        for(int i = 4 + TileBloodInfuser.SLOTS; i < container.getInventory().size(); i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }
}
