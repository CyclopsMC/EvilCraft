package org.cyclops.evilcraft.itemtintsource;

import org.cyclops.cyclopscore.config.extendedconfig.ItemTintSourceConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.ItemWerewolfFlesh;

/**
 * Config for the {@link ItemWerewolfFlesh}
 * @author rubensworks
 *
 */
public class ItemTintSourceBroomPartConfig extends ItemTintSourceConfigCommon<ItemTintSourceBroomPart, IModBase> {

    public ItemTintSourceBroomPartConfig() {
        super(
                EvilCraft._instance,
                "broom_part",
                eConfig -> ItemTintSourceBroomPart.MAP_CODEC
        );
    }
}
