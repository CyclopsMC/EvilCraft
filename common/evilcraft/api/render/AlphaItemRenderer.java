package evilcraft.api.render;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;


/**
 * Allows item to be rendered with a better (alpha) transparency blend.
 * @author rubensworks
 *
 */
public class AlphaItemRenderer implements IItemRenderer{

    /**
     * The ID for this renderer.
     */
    public static int ID = RenderingRegistry.getNextAvailableRenderId();
    private static RenderItem renderItem = new RenderItem();

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
            ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        GL11.glEnable(GL11.GL_BLEND);
        IIcon icon = itemStack.getIconIndex();
        renderItem.renderIcon(0, 0, icon, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
