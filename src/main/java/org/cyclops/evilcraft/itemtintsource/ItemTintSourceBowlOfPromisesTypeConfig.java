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
public class ItemTintSourceBowlOfPromisesTypeConfig extends ItemTintSourceConfigCommon<ItemTintSourceBowlOfPromisesType, IModBase> {

    public ItemTintSourceBowlOfPromisesTypeConfig() {
        super(
                EvilCraft._instance,
                "bowl_of_promises_type",
                eConfig -> ItemTintSourceBowlOfPromisesType.MAP_CODEC
        );
    }
}
