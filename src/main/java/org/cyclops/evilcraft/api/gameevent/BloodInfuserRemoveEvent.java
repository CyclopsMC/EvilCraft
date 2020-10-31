package org.cyclops.evilcraft.api.gameevent;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event when a player removes a crafted item from a slot.
 * @author rubensworks
 */
public class BloodInfuserRemoveEvent extends Event {

    public final PlayerEntity player;
    public final ItemStack output;

    /**
     * Make a new instance.
     * @param player The player removing the item.
     * @param output The item that was picked up from the output slot.
     */
    public BloodInfuserRemoveEvent(PlayerEntity player, ItemStack output) {
        this.player = player;
        this.output = output;
    }

}
