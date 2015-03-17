package evilcraft.client.render.item;

import evilcraft.block.EntangledChaliceItem;
import evilcraft.client.render.model.ModelChalice;
import evilcraft.client.render.tileentity.RenderTileEntityEntangledChalice;
import evilcraft.core.client.render.item.RenderModelWavefrontItem;
import evilcraft.core.client.render.model.ModelWavefront;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;


/**
 * Render the dark tank as item.
 * @author rubensworks
 *
 */
public class RenderItemEntangledChalice extends RenderModelWavefrontItem {
	
    /**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderItemEntangledChalice(ModelWavefront model, ResourceLocation texture) {
        super(model, texture);
    }
    
    @Override
    protected void preRenderModel(final ItemRenderType type, float x, float y, float z) {
    	final EntangledChaliceItem chalice = (EntangledChaliceItem) currentItemStack.getItem();
    	FluidStack fluidStack = chalice.getFluid(currentItemStack);
    	ModelChalice.setColorSeed(chalice.getTankID(currentItemStack));
    	RenderHelpers.renderFluidContext(fluidStack, new BlockPos(0, 0, 0), new IFluidContextRender() {
			
			@Override
			public void renderFluid(FluidStack fluid) {
				if(type == ItemRenderType.ENTITY) {
		    		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		    	}
				double capacity = chalice.getCapacity(currentItemStack);
            	RenderTileEntityEntangledChalice.renderFluidSide(fluid, (double) fluid.amount / capacity);
			}
		});
    }

}
