package evilcraft.client.render.item;

import evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.client.render.item.RenderItemBlock;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.core.helper.RenderHelpers.IFluidContextRender;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.RenderingRegistry;


/**
 * Render the dark tank as item.
 * @author rubensworks
 *
 */
public class RenderItemDarkTank extends RenderItemBlock {
    
    @Override
	protected void preRenderAdditional(ItemRenderType type, final ItemStack itemStack, Block block) {
		// Render the fluid sides
        final IBlockTank tank = (IBlockTank) block;
        if(itemStack.getTagCompound() != null) {
        	FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
        	RenderHelpers.renderFluidContext(fluidStack, new BlockPos(-0.5D, -0.5D, -0.5D), new IFluidContextRender() {
        		@Override
				public void renderFluid(FluidStack fluid) {
        			int capacity = tank.getTankCapacity(itemStack);
                	double height = Math.min(1.0D, ((double) fluid.amount / (double) capacity)) / 1.01D;
                	RenderTileEntityDarkTank.renderFluidSides(height, fluid);
				}
			});
        }
	}

}
