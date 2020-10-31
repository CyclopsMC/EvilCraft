package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Werewolf Bone.
 * @author rubensworks
 *
 */
public class ItemWerewolfBoneConfig extends ItemConfig {

    public ItemWerewolfBoneConfig() {
        super(
                EvilCraft._instance,
            "werewolf_bone",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

    /*
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALBONE; TODO
    }*/
    
}
