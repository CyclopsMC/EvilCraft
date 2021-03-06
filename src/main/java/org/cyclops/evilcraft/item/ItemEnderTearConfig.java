package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Ender Tear.
 * @author rubensworks
 *
 */
public class ItemEnderTearConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "If this item should be injected in loot tables..", requiresMcRestart = true)
    public static boolean injectLootTables = true;

    public ItemEnderTearConfig() {
        super(
                EvilCraft._instance,
            "ender_tear",
                eConfig -> new Item(new Item.Properties()
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .maxStackSize(16))
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        if (injectLootTables) {
            LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/entities/ender_tear"),
                    new ResourceLocation("entities/enderman"));
        }
    }
}
