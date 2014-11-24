package evilcraft.item;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Werewolf Bone.
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
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALBONE;
    }
    
}
