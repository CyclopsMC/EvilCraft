package org.cyclops.evilcraft.item;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.LootHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
        LootHelpers.injectLootTable(new ResourceLocation(Reference.MOD_ID, "inject/entities/ender_tear"),
                LootTableList.ENTITIES_ENDERMAN);
    }
}
