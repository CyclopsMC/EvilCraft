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
public class ItemTintSourceWeatherContainerTypeConfig extends ItemTintSourceConfigCommon<ItemTintSourceWeatherContainerType, IModBase> {

    public ItemTintSourceWeatherContainerTypeConfig() {
        super(
                EvilCraft._instance,
                "weather_container_type",
                eConfig -> ItemTintSourceWeatherContainerType.MAP_CODEC
        );
    }
}
