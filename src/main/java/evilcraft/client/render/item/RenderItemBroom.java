package evilcraft.client.render.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import evilcraft.client.render.model.ModelBroom;
import evilcraft.core.client.render.model.RenderModel;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.item.Broom;

/**
 * Item renderer for the {@link Broom}.
 * @author rubensworks
 *
 */
public class RenderItemBroom extends RenderModel<ModelBroom> implements IItemRenderer {

    /**
     * Make a new instance.
     * @param config The config instance.
     */
    public RenderItemBroom(ExtendedConfig<ItemConfig> config) {
        super(config);
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
    
    @Override
    protected ModelBroom constructModel() {
        return new ModelBroom();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY: {
                renderBroom(0.5F, 0.5F, 0.5F, 0.17F, 0, 0, 0);
                break;
            }
            case EQUIPPED: {
                renderBroom(5.0F, 4.0F, 5.0F, 0.17F, 10F, 50F, -180F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderBroom(1.0F, 1.0F, 1.0F, 0.17F, 10F, -60F, 90F);
                break;
            }
            case INVENTORY: {
                renderBroom(0.0F, -1.0F, 0.0F, 0.088F, 10F, 10F, 0F);
                break;
            }
            default:
                break;
        }
    }
    
    private void renderBroom(float x, float y, float z, float scale, float rotationX, float rotationY, float rotationZ) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(getEntityTexture(null));
        
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(rotationX, 1, 0, 0);
        GL11.glRotatef(rotationY, 0, 1, 0);
        GL11.glRotatef(rotationZ, 0, 0, 1);
        model.render(null, 0, 0, 0, 0, 0, 1.0F);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double d0, double d1, double d2,
            float f, float f1) {
        // Not required here.
    }

}
