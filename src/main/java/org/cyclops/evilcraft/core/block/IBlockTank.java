package org.cyclops.evilcraft.core.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interface for blocks that have a tank.
 * @author rubensworks
 *
 */
public interface IBlockTank {
    /**
     * Get the default capacity possible for this item/blockState type.
     * @return The default capacity.
     */
    public int getDefaultCapacity();
    /**
     * @return If this tank can be activated to auto-fill.
     */
    public boolean isActivatable();
    /**
     * Toggle the activation of the item.
     * @param itemStack The item.
     * @param world The world.
     * @param player The player holding the item.
     * @return The toggled item.
     */
    public ItemStack toggleActivation(ItemStack itemStack, World world, PlayerEntity player);
    /**
     * If the given item is activated.
     * @param itemStack The item.
     * @param world The world.
     * @return If it is activated.
     */
    public boolean isActivated(ItemStack itemStack, World world);
	
}
