package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.ItemConfig;

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
     * Make a new instance.
     */
    public BroomConfig() {
        super(
            Reference.ITEM_BROOM,
            "Broom",
            "broom",
            null,
            Broom.class
        );
    }
    
    @Override
    public void onRegistered() {
        super.onRegistered();
        for(String chestCategory : Helpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(new ItemStack(Broom.getInstance()), 1, 2, 2));
        }
    }
    
}
