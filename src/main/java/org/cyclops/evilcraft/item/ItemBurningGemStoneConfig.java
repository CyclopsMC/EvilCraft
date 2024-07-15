package org.cyclops.evilcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ItemBurningGemStone}.
 * @author rubensworks
 *
 */
public class ItemBurningGemStoneConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "How much damage this item can take.")
    public static int maxDamage = 64;

    public ItemBurningGemStoneConfig() {
        super(
                EvilCraft._instance,
            "burning_gem_stone",
                eConfig -> new ItemBurningGemStone(new Item.Properties()
                        .component(DataComponents.RARITY, Rarity.UNCOMMON)
                        .stacksTo(1)
                        .setNoRepair())
        );
    }

}
