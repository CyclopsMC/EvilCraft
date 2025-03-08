package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemPiercingVengeanceFocus}.
 * @author rubensworks
 *
 */
public class ItemPiercingVengeanceFocusConfig extends ItemConfigCommon<IModBase> {

    public ItemPiercingVengeanceFocusConfig() {
        super(
                EvilCraft._instance,
            "piercing_vengeance_focus",
                (eConfig, properties) -> new ItemPiercingVengeanceFocus(properties
                        .rarity(Rarity.RARE))
        );
    }

}
