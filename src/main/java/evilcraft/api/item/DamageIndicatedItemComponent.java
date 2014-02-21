package evilcraft.api.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
    
    private int fakeMaxDamage = 100;

    /**
     * Create a new DamageIndicatedItemComponent
     * 
     * @param item
     *          The item class on which the behaviour will be added.
     */
    public DamageIndicatedItemComponent(ItemFluidContainer item)
    {
        this.item = item;
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
        if(itemStack.getItem() == item && item.getCapacity(itemStack) > 0 && amount <= item.getCapacity(itemStack)) {
            item.setDamage(itemStack, fakeMaxDamage + 1 - ((amount * fakeMaxDamage) / (item.getCapacity(itemStack))));
        }
    }
    
    /**
     * Add the creative tab items.
     * @param item The item.
     * @param tab The creative tab to add to.
     * @param itemList The item list to add to.
     * @param fluid The fluid in the container that needs to be added.
     * @param meta The meta data for the item to add.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item item, CreativeTabs tab, List itemList, Fluid fluid, int meta) {
        // Add the 'full' container.
        ItemStack itemStackFull = new ItemStack(this.item, 1, meta);
        this.item.fill(itemStackFull, new FluidStack(fluid, this.item.getCapacity(itemStackFull)), true);
        itemList.add(itemStackFull);
        
        // Add the 'empty' container.
        ItemStack itemStackEmpty = new ItemStack(item, 1, meta);
        this.item.fill(itemStackEmpty, new FluidStack(fluid, 0), true);
        itemList.add(itemStackEmpty);
    }
    
    /**
     * Get hovering info for the given {@link ItemStack}.
     * @param itemStack The item stack to add the info for.
     * @return The info for the item.
     */
    public String getInfo(ItemStack itemStack) {
        int amount = 0;
        if(item.getFluid(itemStack) != null)
            amount = item.getFluid(itemStack).amount;
        return getInfo(amount, item.getCapacity(itemStack));
    }
    
    /**
     * Get hovering info for the given amount and capacity.
     * @param amount The amount to show.
     * @param capacity The capacity to show.
     * @return The info generated from the given parameters.
     */
    public static String getInfo(int amount, int capacity) {
        return "" + amount + " / " + capacity + " mB";
    }
    
    /**
     * Add information to the given list for the given item.
     * @param itemStack The {@link ItemStack} to add info for.
     * @param entityPlayer The player that will see the info.
     * @param list The info list where the info will be added.
     * @param par4 No idea...
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        list.add(IInformationProvider.ITEM_PREFIX+((IInformationProvider) itemStack.getItem()).getInfo(itemStack));
    }

    /**
     * Get the displayed damage value for the given {@link ItemStack}.
     * @param itemStack The {@link ItemStack} to get the displayed damage for.
     * @return The displayed damage.
     */
    public int getDisplayDamage(ItemStack itemStack) {
        int amount = 0;
        if(item.getFluid(itemStack) != null)
            amount = item.getFluid(itemStack).amount;
        return fakeMaxDamage + 1 - ((amount * fakeMaxDamage) / (item.getCapacity(itemStack)));
    }
    
}
