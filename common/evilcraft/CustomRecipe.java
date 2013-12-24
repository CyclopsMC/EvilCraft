package evilcraft;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CustomRecipe {
    
    private ItemStack itemStack;
    private FluidStack fluidStack;
    private Block factory;
    private int duration = 20;
    
    public CustomRecipe(ItemStack itemStack, FluidStack fluidStack, Block factory) {
        this(itemStack, fluidStack, factory, 0);
    }
    
    public CustomRecipe(ItemStack itemStack, FluidStack fluidStack, Block factory, int duration) {
        this.itemStack = itemStack;
        this.fluidStack = fluidStack;
        this.factory = factory;
        this.duration = duration;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public Block getFactory() {
        return factory;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof CustomRecipe)) return false;
        CustomRecipe o = (CustomRecipe) object;
        boolean item = this.getItemStack().itemID == o.getItemStack().itemID;
        boolean fluid = true;
        boolean factory = this.getFactory() == o.getFactory();
        
        if(this.getFluidStack() != null) {
            fluid = false;
            if(o.getFluidStack() != null) {
                if(this.getFluidStack().getFluid().equals(o.getFluidStack().getFluid())) {
                    fluid = o.getFluidStack().amount >= this.getFluidStack().amount;
                }
            }
        }
        
        return item && fluid && factory;
    }
}
