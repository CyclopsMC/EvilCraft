package evilcraft.items;

import evilcraft.api.config.ExtendedConfig;

public class WerewolfBoneConfig extends ExtendedConfig {
    
    public static WerewolfBoneConfig _instance;

    public WerewolfBoneConfig() {
        super(
            1,
            "WerewolfBone",
            "werewolfbone",
            null,
            WerewolfBone.class
        );
    }
    
}
