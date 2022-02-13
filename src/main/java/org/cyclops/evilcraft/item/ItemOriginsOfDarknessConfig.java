package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootTables;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class ItemOriginsOfDarknessConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "If this item should be injected in loot tables.", requiresMcRestart = true)
    public static boolean injectLootTables = true;

    public ItemOriginsOfDarknessConfig() {
        super(
                EvilCraft._instance,
            "origins_of_darkness",
                eConfig -> new ItemOriginsOfDarkness(new Item.Properties()
                        .tab(EvilCraft._instance.getDefaultItemGroup())
                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/origins_of_darkness"),
                    LootTables.SPAWN_BONUS_CHEST,
                    LootTables.VILLAGE_TOOLSMITH,
                    LootTables.VILLAGE_WEAPONSMITH,
                    LootTables.VILLAGE_SHEPHERD,
                    LootTables.SIMPLE_DUNGEON,
                    LootTables.ABANDONED_MINESHAFT,
                    LootTables.JUNGLE_TEMPLE,
                    LootTables.IGLOO_CHEST);
        }
    }
}
