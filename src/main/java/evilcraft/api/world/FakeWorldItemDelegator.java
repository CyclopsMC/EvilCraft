package evilcraft.api.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

/**
 * A fake world that can delegate dropped items to an {@link IItemDropListener}.
 * @author rubensworks
 *
 */
public class FakeWorldItemDelegator extends FakeWorld {
	
	private IItemDropListener itemDropListener;

	/**
	 * Make a new instance.
	 * @param itemDropListener The item drops listener.
	 */
	public FakeWorldItemDelegator(IItemDropListener itemDropListener) {
		this.itemDropListener = itemDropListener;
	}
	
	@Override
	public boolean spawnEntityInWorld(Entity entity) {
		if(entity != null && entity instanceof EntityItem) {
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
