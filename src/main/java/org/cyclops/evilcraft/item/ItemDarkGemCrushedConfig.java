package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Crushed Dark Gem.
 * @author rubensworks
 *
 */
public class ItemDarkGemCrushedConfig extends ItemConfig {

    public ItemDarkGemCrushedConfig() {
        super(
                EvilCraft._instance,
                "dark_gem_crushed",
                eConfig -> new Item(new Item.Properties()
                        )
        );
        MinecraftForge.EVENT_BUS.addListener(this::onFurnaceFuelBurnTimeEventEvent);
    }

    public void onFurnaceFuelBurnTimeEventEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().getItem() == this.getInstance()) {
            event.setBurnTime(16000);
        }
    }

}
