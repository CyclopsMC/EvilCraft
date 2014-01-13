package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class WerewolfBoneConfig extends ItemConfig {
    
    public static WerewolfBoneConfig _instance;

    public WerewolfBoneConfig() {
        super(
            Reference.ITEM_WEREWOLFBONE,
            "WerewolfBone",
            "werewolfBone",
            null,
            WerewolfBone.class
        );
    }
    
}
