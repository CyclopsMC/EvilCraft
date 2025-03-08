package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBurningGemStone}.
 * @author rubensworks
 *
 */
public class ItemBurningGemStoneConfig extends ItemConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "item", comment = "How much damage this item can take.")
    public static int maxDamage = 64;

    public ItemBurningGemStoneConfig() {
        super(
                EvilCraft._instance,
            "burning_gem_stone",
                (eConfig, properties) -> new ItemBurningGemStone(properties
                        .component(DataComponents.RARITY, Rarity.UNCOMMON)
                        .stacksTo(1)
                        .setNoCombineRepair())
        );
    }

}
