package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
                EvilCraft._instance,
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
