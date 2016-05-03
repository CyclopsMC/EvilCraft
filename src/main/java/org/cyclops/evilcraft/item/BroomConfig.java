package org.cyclops.evilcraft.item;

import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link Broom}.
 * @author rubensworks
 *
 */
public class BroomConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BroomConfig _instance;
    /**
     * If the broom should spawn in loot chests.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If the broom should spawn in loot chests.")
    public static boolean lootChests = true;

    /**
     * The position to render the broom gui overlay at. (0=NE, 1=SE, 2=SW,3=NW)
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The position to render the broom gui overlay at. (0=NE, 1=SE, 2=SW,3=NW)")
    public static int guiOverlayPosition = 1;
    /**
     * The X offset for the broom gui overlay.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The X offset for the broom gui overlay.")
    public static int guiOverlayPositionOffsetX = 0;
    /**
     * The Y offset for the broom gui overlay.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The Y offset for the broom gui overlay.")
    public static int guiOverlayPositionOffsetY = 0;
    /**
     * The blood usage in mB per tick.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The blood usage in mB per tick.")
    public static int bloodUsage = 1;
    /**
     * The blood usage in mB per block break.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The blood usage in mB per block break.")
    public static int bloodUsageBlockBreak = 1;
    /**
     * Show broom part tooltips on source items.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "Show broom part tooltips on source items.")
    public static boolean broomPartTooltips = true;
    /**
     * Show broom modifier tooltips on source items.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "Show broom modifier tooltips on source items.")
    public static boolean broomModifierTooltips = false;

    /**
     * Make a new instance.
     */
    public BroomConfig() {
        super(
                EvilCraft._instance,
        	true,
            "broom",
            null,
            Broom.class
        );
    }
    
    @Override
    public void onRegistered() {
        super.onRegistered();
        if(BroomConfig.lootChests) {
            for (String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
                ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(Broom.getInstance(), 0, 1, 1, 2));
            }
        }
    }
    
}
