package org.cyclops.evilcraft.item;

import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
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
            "originsOfDarkness",
            null,
            OriginsOfDarkness.class
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        MinecraftHelpers.addVanillaLootChestLootEntry(
                new LootEntryItem(getItemInstance(), 1, 5, new LootFunction[0], new LootCondition[0], getMod().getModId() + ":" + getSubUniqueName()));
    }
    
}
