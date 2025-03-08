package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood-Waxed Coal.
 * @author rubensworks
 *
 */
public class ItemBloodWaxedCoalConfig extends ItemConfigCommon<IModBase> {

    public ItemBloodWaxedCoalConfig() {
        super(
                EvilCraft._instance,
            "blood_waxed_coal",
                (eConfig, properties) -> new Item(properties)
        );
        NeoForge.EVENT_BUS.addListener(this::onFurnaceFuelBurnTimeEventEvent);
    }

    public void onFurnaceFuelBurnTimeEventEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().getItem() == this.getInstance()) {
            event.setBurnTime(3200);
        }
    }

}
