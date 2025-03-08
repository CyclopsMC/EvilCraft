package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Effortless Ring.
 * @author rubensworks
 *
 */
public class ItemEffortlessRingConfig extends ItemConfigCommon<IModBase> {

    public ItemEffortlessRingConfig() {
        super(
                EvilCraft._instance,
            "effortless_ring",
                (eConfig, properties) -> new ItemEffortlessRing(properties
                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }

}
