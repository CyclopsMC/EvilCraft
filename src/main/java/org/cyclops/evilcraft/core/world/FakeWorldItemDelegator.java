package org.cyclops.evilcraft.core.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

/**
 * A fake world that can delegate dropped items to an {@link IItemDropListener}.
 * @author rubensworks
 *
 */
public class FakeWorldItemDelegator extends FakeWorld {
	
	private static FakeWorldItemDelegator _instance = null;
	
	private IItemDropListener itemDropListener;
	
	private FakeWorldItemDelegator() {

	}
	
	/**
	 * Get the unique instance.
	 * @return The unique instance.
	 */
	public static FakeWorldItemDelegator getInstance() {
		if(_instance == null) {
			_instance = new FakeWorldItemDelegator();
		}
		return _instance;
	}
	
	/**
	 * Set the item drop listener.
	 * @param itemDropListener The item drop listener.
	 */
	public void setItemDropListener(IItemDropListener itemDropListener) {
		this.itemDropListener = itemDropListener;
	}
	
	@Override
	public boolean spawnEntityInWorld(Entity entity) {
		if(entity != null && entity instanceof EntityItem && itemDropListener != null) {
			ItemStack itemStack = ((EntityItem) entity).getEntityItem();
			itemDropListener.onItemDrop(itemStack);
		}
		return true;
	}
	
	/**
	 * The listener for item drops in a fake world.
	 * @author rubensworks
	 *
	 */
	public interface IItemDropListener {
		
		/**
		 * When an item has been dropped in the fake world.
		 * @param itemStack The dropped item.
		 */
		public void onItemDrop(ItemStack itemStack);
		
	}
	
}
