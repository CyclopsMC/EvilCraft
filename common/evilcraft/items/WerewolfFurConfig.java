package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class WerewolfFurConfig extends ItemConfig {
    
    public static WerewolfFurConfig _instance;

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
