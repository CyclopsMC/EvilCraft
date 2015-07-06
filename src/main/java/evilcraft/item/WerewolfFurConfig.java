package evilcraft.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

/**
 * Config for the Werewolf Fur.
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
                EvilCraft._instance,
        	true,
            "werewolfFur",
            null,
            null
        );
    }
    
}
