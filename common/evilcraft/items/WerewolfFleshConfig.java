package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class WerewolfFleshConfig extends ItemConfig {
    
    public static WerewolfFleshConfig _instance;

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
