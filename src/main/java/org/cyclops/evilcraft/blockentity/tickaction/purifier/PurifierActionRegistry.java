package org.cyclops.evilcraft.blockentity.tickaction.purifier;

import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierAction;
import org.cyclops.evilcraft.api.tileentity.purifier.IPurifierActionRegistry;
import org.cyclops.evilcraft.blockentity.BlockEntityPurifier;

import java.util.List;

/**
 * @author Ruben Taelman
 */
public class PurifierActionRegistry implements IPurifierActionRegistry {

    private List<IPurifierAction> registry = Lists.newArrayList();

    public PurifierActionRegistry() {
        register(new ToolBadEnchantPurifyAction());
        register(new DisenchantPurifyAction());
        register(new CollectPotionPurifyAction());
    }

    @Override
    public void register(IPurifierAction purifyAction) {
        registry.add(purifyAction);
    }

    @Override
    public boolean isItemValidForMainSlot(ItemStack itemStack) {
        for(IPurifierAction purifyAction : registry) {
            if(purifyAction.isItemValidForMainSlot(itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack) {
        for(IPurifierAction purifyAction : registry) {
            if(purifyAction.isItemValidForAdditionalSlot(itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int canWork(BlockEntityPurifier tile) {
        for (int i = 0; i < registry.size(); i++) {
            IPurifierAction purifyAction = registry.get(i);
            if(purifyAction.canWork(tile)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean work(int actionId, BlockEntityPurifier tile) {
        if(actionId < registry.size()) {
            return registry.get(actionId).work(tile);
        }
        return true;
    }

}
