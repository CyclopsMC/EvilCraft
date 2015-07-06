package evilcraft.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

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
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(new ItemStack(Broom.getInstance()), 1, 2, 2));
        }
        // TODO
        /*if (MinecraftHelpers.isClientSide()) {
            ClientProxy.ITEM_RENDERERS.put(this.getItemInstance(), new RenderItemBroom(this));
        }*/
    }
    
}
