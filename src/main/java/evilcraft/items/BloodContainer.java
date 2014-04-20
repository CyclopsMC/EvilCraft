package evilcraft.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.ItemHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.fluids.Blood;

/**
 * Containers that can container blood. Different types for different metadata.
 * @author rubensworks
 *
 */
public class BloodContainer extends ConfigurableDamageIndicatedItemFluidContainer {
    
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
        list.add(IInformationProvider.INFO_PREFIX+"Shift + Right click to toggle auto-supply.");
        String autoSupply = EnumChatFormatting.RESET+"Disabled";
        if(ItemHelpers.isActivated(itemStack)) {
            autoSupply = EnumChatFormatting.GREEN + "Enabled";
        }
        list.add(EnumChatFormatting.BOLD + StatCollector.translateToLocal(getUnlocalizedName() + ".info.autoSupply") + " " + autoSupply);
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof EntityPlayer && !world.isRemote && ItemHelpers.isActivated(itemStack)) {
            FluidStack tickFluid = this.getFluid(itemStack);
            if(tickFluid != null && tickFluid.amount > 0) {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack held = player.getCurrentEquippedItem();
                if(held != null && held != itemStack && held.getItem() instanceof IFluidContainerItem) {
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
                        this.drain(itemStack, filled, true);
                    }
                }
            }
        }
        super.onUpdate(itemStack, world, entity, par4, par5);
    }

}
