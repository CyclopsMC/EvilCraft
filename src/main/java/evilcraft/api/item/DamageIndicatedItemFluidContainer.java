package evilcraft.api.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;

/**
 * This extension on {@link ItemFluidContainer} will show a damage indicator depending on how full
 * the container is. This can be used to hold certain amounts of Fluids in an Item.
 * When this item is available in a CreativeTab, it will add itself as a full and an empty container.
 * 
 * This container ONLY allows the fluid from the given type.
 * 
 * @author rubensworks
 *
 */
public abstract class DamageIndicatedItemFluidContainer extends ItemFluidContainer implements IInformationProvider {

    protected DamageIndicatedItemComponent component;
    protected Fluid fluid;
    
    /**
     * Create a new DamageIndicatedItemFluidContainer.
     * 
     * @param capacity
     *          The capacity this container will have.
     * @param fluid
     *          The Fluid instance this container must hold.
     */
    public DamageIndicatedItemFluidContainer(int capacity, Fluid fluid) {
        super(0, capacity);
        this.fluid = fluid;
        init();
    }
    
    private void init() {
        component = new DamageIndicatedItemComponent(this);
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if(container != null && container.stackTagCompound != null && container.stackTagCompound.getCompoundTag("Fluid") != null) {
            // Fix for Thermal Expansion
            FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
            if(stack != null && stack.amount <= 0) {
                stack.amount = 0;
                NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
                fluidTag.setInteger("Amount", 0);
                return stack;
            }
        }
        FluidStack fluidStack = super.drain(container, maxDrain, doDrain);
        return fluidStack;
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        int capacityOld = capacity;
        capacity = getCapacity(container);
        int filled = super.fill(container, resource, doFill);
        capacity = capacityOld;
        return filled;
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
        component.getSubItems(item, tab, itemList, fluid, 0);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return component.getInfo(itemStack);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        component.addInformation(itemStack, entityPlayer, list, par4);
        super.addInformation(itemStack, entityPlayer, list, par4);
    }
    
    @Override
    public boolean isDamaged(ItemStack itemStack) {
        return true;
    }
    
    @Override
    public int getDisplayDamage(ItemStack itemStack) {
        return component.getDisplayDamage(itemStack);
    }
    
    /**
     * Get the fluid.
     * @return The fluid.
     */
    public Fluid getFluid() {
        return this.fluid;
    }

}
