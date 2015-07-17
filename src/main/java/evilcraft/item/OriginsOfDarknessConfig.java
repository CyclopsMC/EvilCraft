package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.MinecraftHelpers;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

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
        	true,
            "originsOfDarkness",
            null,
            OriginsOfDarkness.class
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                    new ItemStack(OriginsOfDarkness.getInstance()), 1, 1, 6));
        }
    }
    
}
