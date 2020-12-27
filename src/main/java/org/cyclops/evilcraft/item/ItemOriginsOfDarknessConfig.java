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
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxStackSize(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/origins_of_darkness"),
                    LootTables.CHESTS_SPAWN_BONUS_CHEST,
                    LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH,
                    LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH,
                    LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD,
                    LootTables.CHESTS_SIMPLE_DUNGEON,
                    LootTables.CHESTS_ABANDONED_MINESHAFT,
                    LootTables.CHESTS_JUNGLE_TEMPLE,
                    LootTables.CHESTS_IGLOO_CHEST);
        }
    }
}
