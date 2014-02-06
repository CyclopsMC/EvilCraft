package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.ItemConfig;

public class BroomConfig extends ItemConfig {
    
    public static BroomConfig _instance;

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
