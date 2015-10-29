package evilcraft.modcompat.bloodmagic;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import org.cyclops.evilcraft.fluid.Blood;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
    
    private static String getOwnerName(ItemStack item) {
        if(item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }
        return item.getTagCompound().getString("ownerName");
    }
    
    private static int getCurrentEssence(String owner) {
    	return ClientSoulNetworkHandler.getInstance().getCurrentEssence(owner);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack, int pass){
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
        	String owner = getOwnerName(itemStack);
        	if(owner == null || owner.isEmpty()) {
        		owner = EnumChatFormatting.ITALIC + L10NHelpers.localize(getUnlocalizedName() + ".info.currentOwner.none");
        	}
        	list.add(L10NHelpers.localize(getUnlocalizedName() + ".info.currentOwner", owner));
        }
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	SoulNetworkHandler.checkAndSetItemOwner(itemStack, player);
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
    	String owner = getOwnerName(container);
    	int essence = 0;
    	if(!(owner == null || owner.isEmpty())) {
    		essence = getCurrentEssence(owner);
    	}
    	FluidStack drainedEssence = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, essence);
    	return BloodFluidConverter.getInstance().convert(drainedEssence);
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
    	String owner = getOwnerName(container);
    	if(owner == null || owner.isEmpty()) {
    		return 0;
    	}
    	int essence = getCurrentEssence(owner);
    	FluidStack essenceFluid = BloodFluidConverter.getInstance().convertReverse(AlchemicalWizardry.lifeEssenceFluid, resource);
    	int filled = essenceFluid == null ? 0 : essenceFluid.amount;
    	if(doFill && !MinecraftHelpers.isClientSide()) {
    		SoulNetworkHandler.setCurrentEssence(owner, essence + filled);
    	}
    	return filled;
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
    	String owner = getOwnerName(container);
    	if(owner == null || owner.isEmpty()) {
    		return null;
    	}
    	int essence = getCurrentEssence(owner);
    	FluidStack toDrain = new FluidStack(Blood.getInstance(), maxDrain);
    	FluidStack toDrainEssence = BloodFluidConverter.getInstance().convertReverse(AlchemicalWizardry.lifeEssenceFluid, toDrain);
    	int drainEssence = Math.min(essence, toDrainEssence == null ? 0 : toDrainEssence.amount);
    	if(doDrain && !MinecraftHelpers.isClientSide()) {
    		SoulNetworkHandler.setCurrentEssence(owner, essence - drainEssence);
    	}
    	FluidStack drainedEssence = new FluidStack(AlchemicalWizardry.lifeEssenceFluid, drainEssence);
    	return BloodFluidConverter.getInstance().convert(drainedEssence);
    }

}
