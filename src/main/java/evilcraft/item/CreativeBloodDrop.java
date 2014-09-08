package evilcraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.fluid.Blood;

/**
 * Containers that holds an infinite amount of blood.
 * @author rubensworks
 *
 */
public class CreativeBloodDrop extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static CreativeBloodDrop _instance = null;
    
    private static final int MB_FILL_PERTICK = 1000;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new CreativeBloodDrop(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static CreativeBloodDrop getInstance() {
        return _instance;
    }

    private CreativeBloodDrop(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, MB_FILL_PERTICK, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    public int getCapacity(ItemStack container) {
        return MB_FILL_PERTICK;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            if(!world.isRemote)
            	ItemHelpers.toggleActivation(itemStack);
            return itemStack;
        }
        return super.onItemRightClick(itemStack, world, player);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
        		getUnlocalizedName() + ".info.autoSupply");
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	updateAutoFill(this, itemStack, world, entity);
        super.onUpdate(itemStack, world, entity, par4, par5);
    }
    
    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param item The item type to fill from.
     * @param itemStack The item stack to fill from.
     * @param world The world.
     * @param entity The entity that holds this item.
     */
    public static void updateAutoFill(ItemFluidContainer item, ItemStack itemStack, World world, Entity entity) {
    	if(entity instanceof EntityPlayer && !world.isRemote && ItemHelpers.isActivated(itemStack)) {
            FluidStack tickFluid = item.getFluid(itemStack);
            if(tickFluid != null && tickFluid.amount > 0) {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack held = player.getCurrentEquippedItem();
                if(held != null && held != itemStack && held.getItem() instanceof IFluidContainerItem && !player.isUsingItem()) {
                    IFluidContainerItem fluidContainer = (IFluidContainerItem) held.getItem();
                    FluidStack heldFluid = fluidContainer.getFluid(held);
                    if(/*tickFluid.amount >= MB_FILL_PERTICK Not required for creative mode filling
                            && */(heldFluid == null || (heldFluid != null
                                                    && heldFluid.isFluidEqual(tickFluid)
                                                    && heldFluid.amount < fluidContainer.getCapacity(held)
                                                    )
                               )
                            ) {
                        int filled = fluidContainer.fill(held, new FluidStack(tickFluid.getFluid(), MB_FILL_PERTICK), true);
                        item.drain(itemStack, filled, true);
                    }
                }
            }
        }
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return new FluidStack(getFluid(), maxDrain);
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if(resource == null) {
            return 0;
        } else {
            return resource.amount;
        }
    }
    
    @Override
    public FluidStack getFluid(ItemStack itemStack) {
        return new FluidStack(getFluid(), MB_FILL_PERTICK / 2);
    }
    
    @Override
    public int getDisplayDamage(ItemStack itemStack) {
        return 1;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(new ItemStack(this));
    }

}
