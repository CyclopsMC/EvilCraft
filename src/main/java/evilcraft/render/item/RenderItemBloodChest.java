package evilcraft.render.item;

import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import evilcraft.blocks.BloodChest;
import evilcraft.render.tileentity.TileEntityBloodChestRenderer;

/**
 * Item renderer for the {@link BloodChest}.
 * @author rubensworks
 *
 */
public class RenderItemBloodChest implements IItemRenderer {
    
    private final ModelChest modelChest;

    /**
     * Make a new instance.
     */
    public RenderItemBloodChest() {
        modelChest = new ModelChest();
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
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY: {
                renderChest(0.5F, 0.5F, 0.5F);
                break;
            }
            case EQUIPPED: {
                renderChest(1.0F, 1.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderChest(1.0F, 1.0F, 1.0F);
                break;
            }
            case INVENTORY: {
                renderChest(0.0F, 0.075F, 0.0F);
                break;
            }
            default:
                break;
        }
    }
    
    private void renderChest(float x, float y, float z) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBloodChestRenderer.TEXTURE);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);
        modelChest.renderAll();
        GL11.glPopMatrix();
    }

}
