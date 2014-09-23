package evilcraft.modcompat.bloodmagic;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.fluid.Blood;

/**
 * Can convert soul network life essence to blood.
 * @author rubensworks
 *
 */
public class BoundBloodDrop extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BoundBloodDrop _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BoundBloodDrop(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BoundBloodDrop getInstance() {
        return _instance;
    }

    private BoundBloodDrop(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, FluidContainerRegistry.BUCKET_VOLUME, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(new ItemStack(this));
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
        		getUnlocalizedName() + ".info.autoSupply");
        if(itemStack.getTagCompound() != null) {
        	String owner = EnergyItems.getOwnerName(itemStack);
        	if(owner == null || owner.isEmpty()) {
        		owner = EnumChatFormatting.ITALIC + L10NHelpers.localize(getUnlocalizedName() + ".info.currentOwner.none");
        	}
        	list.add(L10NHelpers.localize(getUnlocalizedName() + ".info.currentOwner", owner));
        }
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	EnergyItems.checkAndSetItemOwner(itemStack, player);
    	if(player.isSneaking()) {
            if(!world.isRemote)
            	ItemHelpers.toggleActivation(itemStack);
            return itemStack;
        }
        return super.onItemRightClick(itemStack, world, player);
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	if(ItemHelpers.isActivated(itemStack)) {
    		ItemHelpers.updateAutoFill(this, itemStack, world, entity);
    	}
        super.onUpdate(itemStack, world, entity, par4, par5);
    }
    
    @Override
    public int getCapacity(ItemStack container) {
    	FluidStack contents = getFluid(container);
    	int contentsAmount = contents == null ? 0 : contents.amount;
    	return Math.max(contentsAmount, BoundBloodDropConfig.maxCapacity);
    }
    
    @Override
    public FluidStack getFluid(ItemStack container) {
    	String owner = EnergyItems.getOwnerName(container);
    	if(owner == null || owner.isEmpty()) {
    		return new FluidStack(AlchemicalWizardry.lifeEssenceFluid, 0);
    	}
    	int essence = EnergyItems.getCurrentEssence(owner);
    	FluidStack drainedEssence = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, essence);
    	return BloodFluidConverter.getInstance().convert(drainedEssence);
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
    	String owner = EnergyItems.getOwnerName(container);
    	if(owner == null || owner.isEmpty()) {
    		return 0;
    	}
    	int essence = EnergyItems.getCurrentEssence(owner);
    	FluidStack essenceFluid = BloodFluidConverter.getInstance().convertReverse(AlchemicalWizardry.lifeEssenceFluid, resource);
    	int filled = essenceFluid == null ? 0 : essenceFluid.amount;
    	if(doFill && !MinecraftHelpers.isClientSide()) {
    		EnergyItems.setCurrentEssence(owner, essence + filled);
    	}
    	return filled;
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
    	String owner = EnergyItems.getOwnerName(container);
    	if(owner == null || owner.isEmpty()) {
    		return null;
    	}
    	int essence = EnergyItems.getCurrentEssence(owner);
    	FluidStack toDrain = new FluidStack(Blood.getInstance(), maxDrain);
    	FluidStack toDrainEssence = BloodFluidConverter.getInstance().convertReverse(AlchemicalWizardry.lifeEssenceFluid, toDrain);
    	int drainEssence = Math.min(essence, toDrainEssence == null ? 0 : toDrainEssence.amount);
    	if(doDrain && !MinecraftHelpers.isClientSide()) {
    		EnergyItems.setCurrentEssence(owner, essence - drainEssence);
    	}
    	FluidStack drainedEssence = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, drainEssence);
    	return BloodFluidConverter.getInstance().convert(drainedEssence);
    }

}
