package evilcraft.render.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

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
		return type.equals(ItemRenderType.ENTITY);
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Grenade grenade = Grenade.getInstance();
		
		GL11.glPushMatrix();

		switch(type) {
	        case INVENTORY: {
	        	renderInventory(grenade.getIcon(item, 0));
	            break;
	        }
	        case ENTITY: {
	        	renderEntity(grenade.getIcon(item, 0), grenade.getTextureLocation(item));
	        	break;
	        }
	        default:
		}

		GL11.glPopMatrix();
	}
	
	private void renderEntity(IIcon icon, ResourceLocation textureLocation) {
		GL11.glTranslatef(-0.5f, 0f, 0f);
		ItemRenderer.renderItemIn2D(Tessellator.instance,
				icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(),
				icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
	}
	
	private void renderInventory(IIcon icon) {
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		renderItem.renderIcon(0, 0, icon, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
