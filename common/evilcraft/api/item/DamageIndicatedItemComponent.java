package evilcraft.api.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.api.IInformationProvider;

/**
 * A component that has to be added for classes that want to implement the DamageIndicator behaviour.
 * 
 * Items can add this component (Composite design-pattern) to any item that needs to have a damage
 * indicator based on a custom value. Like for example the amount of energy left in an IC2 electrical
 * wrench, or the amount of MJ's left in a redstone energy cell from Thermal Expansion.
 * 
 * See {@link DamageIndicatedItemFluidContainer} for an example.
 * This could be for example an Item or an ItemFluidContainer.
 * 
 * @author rubensworks
 *
 */
public class DamageIndicatedItemComponent{
    
    /**
     * The item class on which the behaviour will be added.
     */
    public ItemFluidContainer item;
    /**
     * The custom defined capacity this damage indicator must have.
     */
    public int capacity;
    
    private int fakeMaxDamage = 100;

    /**
     * Create a new DamageIndicatedItemComponent
     * 
     * @param item
     *          The item class on which the behaviour will be added.
     * @param capacity
     *          The custom defined capacity this damage indicator must have.
     */
    public DamageIndicatedItemComponent(ItemFluidContainer item, int capacity)
    {
        this.item = item;
        this.capacity = capacity;
        item.setMaxStackSize(1);
        item.setMaxDamage(fakeMaxDamage + 2);
        item.setNoRepair();
    }
    
    /**
     * Updates the amount of the given stack with the given amount depending on the predefined item.
     * This method should be called whenever the contained amount of this container should be updated, 
     * together with the damage indicator.
     * Deprecated, no damage values are used anymore for storing damage bar values.
     * Using this anyways will break stuff if your item is meta data based.
     * 
     * @param itemStack
     *          The itemStack that will get an updated damage bar
     * @param amount
     *          The new amount this damage indicator must hold for the given itemStack.
     */
    @Deprecated
    public void updateAmount(ItemStack itemStack, int amount) {
        if(itemStack.getItem() == item && capacity > 0 && amount <= capacity) {
            item.setDamage(itemStack, fakeMaxDamage + 1 - ((amount * fakeMaxDamage) / (capacity)));
        }
    }
    
    @SuppressWarnings("unchecked")
    public void getSubItems(int id, CreativeTabs tab, List itemList, Fluid fluid) {
        // Add the 'full' container.
        ItemStack itemStackFull = new ItemStack(item, 1);
        item.fill(itemStackFull, new FluidStack(fluid, capacity), true);
        itemList.add(itemStackFull);
        
        // Add the 'empty' container.
        ItemStack itemStackEmpty = new ItemStack(item, 1);
        item.fill(itemStackEmpty, new FluidStack(fluid, 0), true);
        itemList.add(itemStackEmpty);
    }
    
    public String getInfo(ItemStack itemStack) {
        int amount = 0;
        if(item.getFluid(itemStack) != null)
            amount = item.getFluid(itemStack).amount;
        return getInfo(amount, this.capacity);
    }
    
    public static String getInfo(int amount, int capacity) {
        return "" + amount + " / " + capacity + " mB";
    }
    
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        list.add(IInformationProvider.ITEM_PREFIX+((IInformationProvider) itemStack.getItem()).getInfo(itemStack));
    }

    public int getDisplayDamage(ItemStack itemStack) {
        int amount = 0;
        if(item.getFluid(itemStack) != null)
            amount = item.getFluid(itemStack).amount;
        return fakeMaxDamage + 1 - ((amount * fakeMaxDamage) / (capacity));
    }
    
}
