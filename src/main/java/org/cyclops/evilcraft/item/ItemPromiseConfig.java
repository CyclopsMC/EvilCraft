package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;

/**
 * Config for the {@link ItemPromise}.
 * @author rubensworks
 *
 */
public class ItemPromiseConfig extends ItemConfigCommon<IModBase> {

    public ItemPromiseConfig(Upgrades.Upgrade upgrade) {
        super(
                EvilCraft._instance,
            "promise_" + upgrade.getId() + "_" + upgrade.getTier(),
                (eConfig, properties) -> new ItemPromise(properties
                        .component(DataComponents.RARITY, upgrade.getTier() > 0 ? Rarity.RARE : Rarity.UNCOMMON)
                        .stacksTo(4), upgrade)
        );
    }
}
