package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the {@link WerewolfFlesh}
 * @author rubensworks
 *
 */
public class WerewolfFleshConfig extends ItemConfig {

    /**
     * Humanoid flesh will drop in a 1/X chance.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "Humanoid flesh will drop in a 1/X chance.", isCommandable = true)
    public static int humanoidFleshDropChance = 5;

    /**
     * The unique instance.
     */
    public static WerewolfFleshConfig _instance;

    /**
     * Make a new instance.
     */
    public WerewolfFleshConfig() {
        super(
                EvilCraft._instance,
        	true,
            "werewolfFlesh",
            null,
            WerewolfFlesh.class
        );
    }

    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_FLESH;
    }
}
