package evilcraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
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
import evilcraft.core.recipe.event.IRecipeOutputObserver;
import evilcraft.fluid.Blood;

/**
 * Containers that can container blood. Different types for different metadata.
 * @author rubensworks
 *
 */
public class BloodContainer extends ConfigurableDamageIndicatedItemFluidContainer implements IRecipeOutputObserver {
    
    private static BloodContainer _instance = null;
    
    private IIcon[] icons = new IIcon[BloodContainerConfig.getContainerLevels()];
    
    private static final int MB_FILL_PERTICK = 10;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodContainer(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodContainer getInstance() {
        return _instance;
    }

    private BloodContainer(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, BloodContainerConfig.containerSizeBase, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for(int i = 0; i < icons.length; i++) {
            icons[i] = iconRegister.registerIcon(getIconString() + "_" + i);
        }
    }
    
    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[Math.min(damage & 7, icons.length - 1)];
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
        for(int i = 0; i < icons.length; i++) {
            component.getSubItems(item, tab, itemList, fluid, i);
        }
    }
    
    @Override
    public int getCapacity(ItemStack container) {
        if(isCreativeItem(container)) {
            return Integer.MAX_VALUE;
        }
        return capacity << (container.getItemDamage() & 7);
    }
    
    //getItemDisplayName
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + BloodContainerConfig.containerLevelNames[Math.min(itemStack.getItemDamage() & 7, icons.length - 1)];
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
                    if(tickFluid.amount >= MB_FILL_PERTICK
                            && (heldFluid == null || (heldFluid != null
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
    
    /**
     * Check if the given item is a creative-only container.
     * @param itemStack The item to check.
     * @return If it is creative-only.
     */
    public boolean isCreativeItem(ItemStack itemStack) {
        return itemStack.getItemDamage() == BloodContainerConfig.getContainerLevels() - 1;
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if(isCreativeItem(container)) {
            return new FluidStack(getFluid(), maxDrain);
        }
        return super.drain(container, maxDrain, doDrain);
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if(isCreativeItem(container)) {
            if(resource == null) {
                return 0;
            } else {
                return resource.amount;
            }
        }
        return super.fill(container, resource, doFill);
    }
    
    @Override
    public FluidStack getFluid(ItemStack itemStack) {
        if(isCreativeItem(itemStack)) {
            return new FluidStack(getFluid(), Integer.MAX_VALUE / 2);
        }
        return super.getFluid(itemStack);
    }
    
    @Override
    public int getDisplayDamage(ItemStack itemStack) {
        if(isCreativeItem(itemStack)) {
            return 0;
        }
        return super.getDamage(itemStack);
    }
    
    @Override
	public ItemStack getRecipeOutput(InventoryCrafting craftingGrid,
			ItemStack output) {
		for(int i = 0; i < craftingGrid.getSizeInventory(); i++) {           
            if(craftingGrid.getStackInSlot(i) != null) {
                ItemStack input = craftingGrid.getStackInSlot(i);
                if(input.getItem() != null && input.getItem() == BloodContainer.getInstance()) {
                    FluidStack inputFluid = BloodContainer.getInstance().getFluid(input);
                    BloodContainer.getInstance().fill(output, inputFluid, true);
                }
            }  
        }
		return output;
	}

}
