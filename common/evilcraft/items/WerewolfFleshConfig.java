package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link WerewolfFlesh}
 * @author rubensworks
 *
 */
public class WerewolfFleshConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static WerewolfFleshConfig _instance;

    /**
     * Make a new instance.
     */
    public WerewolfFleshConfig() {
        super(
            Reference.ITEM_WEREWOLFFLESH,
            "Werewolf Flesh",
            "werewolfFlesh",
            null,
            WerewolfFlesh.class
        );
    }
    
}
