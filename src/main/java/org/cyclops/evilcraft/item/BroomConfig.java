package org.cyclops.evilcraft.item;

import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.broom.BroomParts;

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
        // TODO: for now, just load the broom parts in a hacky way
        Object _unused = BroomParts.ROD;
    }
    
}
