package org.cyclops.evilcraft.item;

import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Ender Tear.
 * @author rubensworks
 *
 */
public class EnderTearConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static EnderTearConfig _instance;

    /**
     * The 1/X chance on dropping this item.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The 1/X chance on dropping this item.")
    public static int chanceDrop = 10;

    /**
     * The amount of liquid ender produced when TE or TCon is available.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of liquid ender produced when TE or TCon is available.", requiresMcRestart = true)
    public static int mbLiquidEnder = 2000;

    /**
     * Make a new instance.
     */
    public EnderTearConfig() {
        super(
                EvilCraft._instance,
            true,
            "ender_tear",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return(ConfigurableItem) new ConfigurableItem(this).setMaxStackSize(16);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        LootHelpers.addLootPool(LootTableList.ENTITIES_ENDERMAN, new LootPool(new LootEntry[]{
                new LootEntryItem(getItemInstance(), 1, 1, new LootFunction[0], new LootCondition[]{
                        (rand, context) -> {
                            int chance = chanceDrop;
                            if (context.getLootingModifier() > 0) {
                                chance /= context.getLootingModifier() + 1;
                            }
                            return chance > 0 && rand.nextInt(chance) == 0;
                        }
                }, "ender_tear")
        }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "ender_tear"));
    }
}
