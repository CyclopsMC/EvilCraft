package evilcraft.api.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;

/**
 * This container ONLY allows fluids from the given type.
 * @author rubensworks
 *
 */
public abstract class DamageIndicatedItemFluidContainer extends ItemFluidContainer implements IDamageIndicatedItem{

    private DamageIndicatedItemComponent component;
    private Fluid fluid;
    
    public DamageIndicatedItemFluidContainer(int itemID, Fluid fluid)
    {
        super(itemID);
        this.fluid = fluid;
        init();
    }
    
    public DamageIndicatedItemFluidContainer(int itemID, int capacity, Fluid fluid)
    {
        super(itemID, capacity);
        this.fluid = fluid;
        init();
    }
    
    private void init()
    {
        component = new DamageIndicatedItemComponent(this, this.capacity);
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        FluidStack fluidStack = super.drain(container, maxDrain, doDrain);
        component.updateDamage(container, fluidStack.amount);
        return fluidStack;
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        int filled = super.fill(container, resource, doFill);
        component.updateDamage(container, this.getFluid(container).amount);
        return filled;
    }
    
    /**
     * Make sure the full and empty container is available is the CreativeTab
     */
    @Override
    public void getSubItems(int id, CreativeTabs tab, List itemList)
    {
        ItemStack itemStackFull = new ItemStack(this, 1);
        component.updateDamage(itemStackFull, capacity);
        this.fill(itemStackFull, new FluidStack(fluid, component.capacity), true);
        itemList.add(itemStackFull);
        
        ItemStack itemStackEmpty = new ItemStack(this, 1);
        component.updateDamage(itemStackEmpty, 0);
        itemList.add(itemStackEmpty);
        //component.getSubItems(id, tab, itemList);
    }

}
