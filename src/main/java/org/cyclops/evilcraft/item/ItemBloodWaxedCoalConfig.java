package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood-Waxed Coal.
 * @author rubensworks
 *
 */
public class ItemBloodWaxedCoalConfig extends ItemConfigCommon<IModBase> {

    public ItemBloodWaxedCoalConfig() {
        super(
                EvilCraft._instance,
            "blood_waxed_coal",
                (eConfig, properties) -> new Item(properties.component(DataComponents.COOKING_FUEL, new CookingFuel(new ResolvableInt.Constant(3200),
                        ResolvableFloat.fromKey(ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER))))
        );
    }


}
