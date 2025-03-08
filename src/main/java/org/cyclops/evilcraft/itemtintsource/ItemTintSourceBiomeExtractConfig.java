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
public class ItemTintSourceBiomeExtractConfig extends ItemTintSourceConfigCommon<ItemTintSourceBiomeExtract, IModBase> {

    public ItemTintSourceBiomeExtractConfig() {
        super(
                EvilCraft._instance,
                "biome_extract",
                eConfig -> ItemTintSourceBiomeExtract.MAP_CODEC
        );
    }
}
