package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link WerewolfFur}.
 * @author rubensworks
 *
 */
public class WerewolfFurConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static WerewolfFurConfig _instance;

    /**
     * Make a new instance.
     */
    public WerewolfFurConfig() {
        super(
            Reference.ITEM_WEREWOLFFUR,
            "Werewolf Fur",
            "werewolfFur",
            null,
            WerewolfFur.class
        );
    }
    
}
