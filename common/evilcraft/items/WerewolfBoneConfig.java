package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class WerewolfBoneConfig extends ExtendedConfig {
    
    public static WerewolfBoneConfig _instance;

    public WerewolfBoneConfig() {
        super(
            Reference.ITEM_WEREWOLFBONE,
            "WerewolfBone",
            "werewolfbone",
            null,
            WerewolfBone.class
        );
    }
    
}
