package evilcraft.api.gameevent;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Event when a player removes a crafted item from a slot.
 * @author rubensworks
 */
public class BloodInfuserRemoveEvent extends Event {

    public final EntityPlayer player;
    public final ItemStack output;

    /**
     * Make a new instance.
     * @param player The player removing the item.
     * @param output The item that was picked up from the output slot.
     */
    public BloodInfuserRemoveEvent(EntityPlayer player, ItemStack output) {
        this.player = player;
        this.output = output;
    }

}
