package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link Kineticator}.
 * @author rubensworks
 *
 */
public class KineticatorConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static KineticatorConfig _instance;
    
    /**
     * If the Kineticator should also attract XP orbs.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If the Kineticator should also attract XP orbs.", isCommandable = true)
    public static boolean moveXP = true;

    /**
     * The amount of ticks in between each area checking for items.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of ticks inbetween each area checking for items.", isCommandable = true)
    public static int tickHoldoff = 1;

    /**
     * The amount of ticks in between each blood consumption when there are valid items in the area.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of ticks in between each blood consumption when there are valid items in the area.", isCommandable = true)
    public static int consumeHoldoff = 20;

    /**
     * The blacklisted items which should not be influenced by the Kineticator, by unique item/blockState name.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB,
            comment = "The blacklisted items which should not be influenced by the Kineticator, by unique item/blockState name.")
    public static String[] kineticateBlacklist = new String[]{
            "appliedenergistics2:item.ItemCrystalSeed",
    };

    /**
     * Make a new instance.
     */
    public KineticatorConfig() {
        super(
                EvilCraft._instance,
        	true,
            "kineticator",
            null,
            Kineticator.class
        );
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if (((Kineticator) getItemInstance()).isRepelling(itemStack)) {
            return super.getModelName(itemStack) + "_repelling";
        }
        return super.getModelName(itemStack);
    }
    
}
