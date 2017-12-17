package org.cyclops.evilcraft.item;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;

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
        LootEntryItem lootEntry = new LootEntryItem(getItemInstance(), 1, 5, new LootFunction[0],
                new LootCondition[0], getMod().getModId() + ":" + getSubUniqueName());
        LootPool lootPool = new LootPool(new LootEntry[]{lootEntry},
                new LootCondition[]{(rand, context) -> rand.nextInt(2) == 0}, new RandomValueRange(1),
                new RandomValueRange(0), "origins_of_darkness");
        LootHelpers.addLootPool(LootTableList.CHESTS_SPAWN_BONUS_CHEST, lootPool);
        LootHelpers.addLootPool(LootTableList.CHESTS_VILLAGE_BLACKSMITH, lootPool);
        LootHelpers.addLootPool(LootTableList.CHESTS_SIMPLE_DUNGEON, lootPool);
        LootHelpers.addLootPool(LootTableList.CHESTS_ABANDONED_MINESHAFT, lootPool);
        LootHelpers.addLootPool(LootTableList.CHESTS_JUNGLE_TEMPLE, lootPool);
        LootHelpers.addLootPool(LootTableList.CHESTS_IGLOO_CHEST, lootPool);
    }
}
