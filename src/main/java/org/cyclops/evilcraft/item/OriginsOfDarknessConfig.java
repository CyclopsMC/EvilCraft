package org.cyclops.evilcraft.item;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class OriginsOfDarknessConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static OriginsOfDarknessConfig _instance;

    /**
     * If this item should be injected in loot tables.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If this item should be injected in loot tables..", requiresMcRestart = true)
    public static boolean injectLootTables = true;

    /**
     * Make a new instance.
     */
    public OriginsOfDarknessConfig() {
        super(
                EvilCraft._instance,
        	true,
            "origins_of_darkness",
            null,
            OriginsOfDarkness.class
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/chests/origins_of_darkness"),
                    LootTableList.CHESTS_SPAWN_BONUS_CHEST,
                    LootTableList.CHESTS_VILLAGE_BLACKSMITH,
                    LootTableList.CHESTS_SIMPLE_DUNGEON,
                    LootTableList.CHESTS_ABANDONED_MINESHAFT,
                    LootTableList.CHESTS_JUNGLE_TEMPLE,
                    LootTableList.CHESTS_IGLOO_CHEST);
        }
    }
}
