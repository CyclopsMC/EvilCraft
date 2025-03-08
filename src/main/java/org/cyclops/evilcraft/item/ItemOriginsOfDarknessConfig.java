package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class ItemOriginsOfDarknessConfig extends ItemConfigCommon<IModBase> {

    public ItemOriginsOfDarknessConfig() {
        super(
                EvilCraft._instance,
            "origins_of_darkness",
                (eConfig, properties) -> new ItemOriginsOfDarkness(properties
                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }
}
