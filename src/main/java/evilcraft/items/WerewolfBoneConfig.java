package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link WerewolfBone}.
 * @author rubensworks
 *
 */
public class WerewolfBoneConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static WerewolfBoneConfig _instance;

    /**
     * Make a new instance.
     */
    public WerewolfBoneConfig() {
        super(
        	true,
            "werewolfBone",
            null,
            WerewolfBone.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALBONE;
    }
    
}
