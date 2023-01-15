package org.cyclops.evilcraft.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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

                        .stacksTo(1)
                        .rarity(Rarity.UNCOMMON))
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/origins_of_darkness"),
                    BuiltInLootTables.SPAWN_BONUS_CHEST,
                    BuiltInLootTables.VILLAGE_TOOLSMITH,
                    BuiltInLootTables.VILLAGE_WEAPONSMITH,
                    BuiltInLootTables.VILLAGE_SHEPHERD,
                    BuiltInLootTables.SIMPLE_DUNGEON,
                    BuiltInLootTables.ABANDONED_MINESHAFT,
                    BuiltInLootTables.JUNGLE_TEMPLE,
                    BuiltInLootTables.IGLOO_CHEST);
        }
    }
}
