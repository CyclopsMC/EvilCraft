package evilcraft.api.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * General item renderer for blocks/items with models.
 * @author rubensworks
 *
 */
public class RenderItemModel implements IItemRenderer {
	
	private final ModelBase model;
	private final ResourceLocation texture;

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderItemModel(ModelBase model, ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
            ItemRendererHelper helper) {
        return true;
    }
    
    /**
     * Get the texture.
     * @return The texture.
     */
    public ResourceLocation getTexture() {
		return texture;
	}

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY: {
                renderModel(0.5F, 0.5F, 0.5F);
                break;
            }
            case EQUIPPED: {
                renderModel(1.0F, 1.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderModel(1.0F, 1.0F, 1.0F);
                break;
            }
            case INVENTORY: {
                renderModel(0.0F, 0.075F, 0.0F);
                break;
            }
            default:
                break;
        }
    }
    
    private void renderModel(float x, float y, float z) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(getTexture());

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);
        renderModel(model);
        GL11.glPopMatrix();
    }
    
    /**
     * Render the actual model, override this to change the way the model should be rendered.
     */
    protected void renderModel(ModelBase model) {
    	model.render(null, 0, 0, 0, 0, 0, 0);
    }
    
}
