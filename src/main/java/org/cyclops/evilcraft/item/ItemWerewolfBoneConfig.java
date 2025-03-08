package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Werewolf Bone.
 * @author rubensworks
 *
 */
public class ItemWerewolfBoneConfig extends ItemConfigCommon<IModBase> {

    public ItemWerewolfBoneConfig() {
        super(
                EvilCraft._instance,
            "werewolf_bone",
                (eConfig, properties) -> new Item(properties)
        );
    }

}
