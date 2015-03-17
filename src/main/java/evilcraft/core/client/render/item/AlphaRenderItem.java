package evilcraft.core.client.render.item;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.opengl.GL11;


/**
 * Allows item to be rendered with a better (alpha) transparency blend.
 * @author rubensworks
 *
 */
public class AlphaRenderItem implements IItemRenderer{

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
        TextureAtlasSprite icon = itemStack.getIconIndex();
        renderItem.renderIcon(0, 0, icon, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
