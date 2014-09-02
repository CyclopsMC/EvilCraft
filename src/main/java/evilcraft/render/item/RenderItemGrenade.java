package evilcraft.render.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import evilcraft.items.Grenade;

/**
 * Renders {@link Grenade}S in the inventory and when dropped as EntityItems.
 * @author immortaleeb
 *
 */
public class RenderItemGrenade implements IItemRenderer {
	
	private static RenderItem renderItem = new RenderItem();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Grenade grenade = Grenade.getInstance();
		
		switch(type) {
	        case INVENTORY: {
	        	renderInventory(grenade.getIcon(item, 0));
	            break;
	        }
	        case ENTITY: {
	        	renderEntity(grenade.getTextureLocation(item));
	        	break;
	        }
	        default:
		}
	}
	
	private void renderEntity(ResourceLocation textureLocation) {
		// FIXME when an item is dropped, it does not rotate like the other dropped items...
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureLocation);
		ItemRenderer.renderItemIn2D(Tessellator.instance, 0f, 0f, 1f, 1f, 0, 0, 0.05f);
	}
	
	private void renderInventory(IIcon icon) {
		GL11.glEnable(GL11.GL_BLEND);
		renderItem.renderIcon(0, 0, icon, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
