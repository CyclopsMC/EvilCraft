package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
public class ExaltedCrafterConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static ExaltedCrafterConfig _instance;
    
    /**
     * If shift clicking on an item should first try to go into the crafting grid.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If shift clicking on an item should first try to go into the crafting grid.", isCommandable = true)
    public static boolean shiftCraftingGrid = false;

    /**
     * Make a new instance.
     */
    public ExaltedCrafterConfig() {
        super(
                EvilCraft._instance,
        	true,
            "exaltedCrafter",
            null,
            ExaltedCrafter.class
        );
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if (((ExaltedCrafter) getItemInstance()).isWooden(itemStack)) {
            return super.getModelName(itemStack) + "_wooden";
        }
        return super.getModelName(itemStack);
    }
    
}
