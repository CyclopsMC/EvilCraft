package org.cyclops.evilcraft.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Garmonbozia.
 * @author rubensworks
 *
 */
public class GarmonboziaConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static GarmonboziaConfig _instance;

    /**
     * Make a new instance.
     */
    public GarmonboziaConfig() {
        super(
                EvilCraft._instance,
            true,
            "garmonbozia",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this) {
            @Override
            public EnumRarity getRarity(ItemStack itemStack) {
                return EnumRarity.EPIC;
            }

            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        };
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        MinecraftHelpers.addVanillaLootChestLootEntry(
                new LootEntryItem(getItemInstance(), 2, 2, new LootFunction[0], new LootCondition[0], getMod().getModId() + ":" + getSubUniqueName()));
    }
    
}
