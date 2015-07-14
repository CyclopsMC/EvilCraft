package evilcraft.core.client.render.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * General item renderer for blocks/items with models.
 * @author rubensworks
 *
 */
public class RenderModelItem implements IItemRenderer {
	
	private final ModelBase model;
	private final ResourceLocation texture;
	
	protected ItemStack currentItemStack;

	/**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderModelItem(ModelBase model, ResourceLocation texture) {
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
    	this.currentItemStack = item;
        switch (type) {
            case ENTITY: {
                renderModel(type, 0.5F, 0.5F, 0.5F);
                break;
            }
            case EQUIPPED: {
                renderModel(type, 1.0F, 1.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderModel(type, 1.0F, 1.0F, 1.0F);
                break;
            }
            case INVENTORY: {
                renderModel(type, 1.0F, 1.0F, 1.0F);
                break;
            }
            default:
                break;
        }
    }
    
    protected void preRenderModel(ItemRenderType type, float x, float y, float z) {
    	
    }
    
    protected void postRenderModel(ItemRenderType type, float x, float y, float z) {
    	
    }
    
    private void renderModel(ItemRenderType type, float x, float y, float z) {
        preRenderModel(type, x, y, z);
        
        if(getTexture() != null) FMLClientHandler.instance().getClient().renderEngine.bindTexture(getTexture());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(-90, 0, 1, 0);
        renderModel(model);
        GlStateManager.popMatrix();
        
        postRenderModel(type, x, y, z);
    }
    
    /**
     * Render the actual model, override this to change the way the model should be rendered.
     * @param model The base model.
     */
    protected void renderModel(ModelBase model) {
    	model.render(null, 0, 0, 0, 0, 0, 0);
    }
    
}
