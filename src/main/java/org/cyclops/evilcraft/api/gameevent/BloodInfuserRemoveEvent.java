package org.cyclops.evilcraft.api.gameevent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event when a player removes a crafted item from a slot.
 * @author rubensworks
 */
public class BloodInfuserRemoveEvent extends Event {

    public final Player player;
    public final ItemStack output;

    /**
     * Make a new instance.
     * @param player The player removing the item.
     * @param output The item that was picked up from the output slot.
     */
    public BloodInfuserRemoveEvent(Player player, ItemStack output) {
        this.player = player;
        this.output = output;
    }

}
