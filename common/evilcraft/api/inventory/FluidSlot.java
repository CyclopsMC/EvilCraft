package evilcraft.api.inventory;

import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidSlot {
    
    final public int x, y;
    public Fluid fluid;

    public FluidSlot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*public void drawSprite(int cornerX, int cornerY) {
        if (fluid != null)
            FluidRenderer.setColorForFluidStack(new FluidStack(fluid, 100));
        
        if (!isDefined())
            return;

        if (getItemStack() != null) {
            drawStack(getItemStack());
        } else if (getIcon() != null) {
            mc.renderEngine.bindTexture(getTexture());
            //System.out.printf("Drawing advanced sprite %s (%d,%d) at %d %d\n", getIcon().getIconName(), getIcon().getOriginX(),getIcon().getOriginY(),cornerX + x, cornerY + y);
            drawTexturedModelRectFromIcon(cornerX + x, cornerY + y, getIcon(), 16, 16);
        }
    }

    public Icon getIcon() {
        return FluidRenderer.getFluidTexture(fluid, false);
    }

    public ResourceLocation getTexture() {
        return FluidRenderer.getFluidSheet(fluid);
    }*/
}
