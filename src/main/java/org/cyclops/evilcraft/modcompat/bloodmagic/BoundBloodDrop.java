package org.cyclops.evilcraft.modcompat.bloodmagic;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.fluid.Blood;

import java.util.List;

/**
 * Can convert soul network life essence to blood.
 * @author rubensworks
 *
 */
public class BoundBloodDrop extends ConfigurableDamageIndicatedItemFluidContainer implements IBindable {
    
    private static BoundBloodDrop _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BoundBloodDrop getInstance() {
        return _instance;
    }

    public BoundBloodDrop(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, FluidContainerRegistry.BUCKET_VOLUME, Blood.getInstance());
        setPlaceFluids(true);
    }

    @Override
    public String getOwnerName(ItemStack item) {
        if(item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }
        return item.getTagCompound().getString(Constants.NBT.OWNER_NAME);
    }

    @Override
    public String getOwnerUUID(ItemStack item) {
        if(item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }
        return item.getTagCompound().getString(Constants.NBT.OWNER_UUID);
    }

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }

    private static int getCurrentEssence(String uuid) {
    	return ClientSoulNetworkHandler.getInstance().getCurrentEssence(uuid);
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
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
        		owner = TextFormatting.ITALIC + L10NHelpers.localize(getUnlocalizedName() + ".info.currentOwner.none");
        	}
        	list.add(L10NHelpers.localize(getUnlocalizedName() + ".info.currentOwner", owner));
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
    	if(player.isSneaking()) {
            if(!world.isRemote)
            	ItemHelpers.toggleActivation(itemStack);
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
        }
        return super.onItemRightClick(itemStack, world, player, hand);
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
    	String uuid = getOwnerUUID(container);
    	int essence = 0;
    	if(!(uuid == null || uuid.isEmpty())) {
    		essence = getCurrentEssence(uuid);
    	}
    	FluidStack drainedEssence = new FluidStack(BloodMagicAPI.getLifeEssence(), essence);
    	return BloodFluidConverter.getInstance().convert(drainedEssence);
    }
    
    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
    	String uuid = getOwnerUUID(container);
    	if(uuid == null || uuid.isEmpty()) {
    		return 0;
    	}
    	int essence = getCurrentEssence(uuid);
        FluidStack essenceFluid = BloodFluidConverter.getInstance().convertReverse(BloodMagicAPI.getLifeEssence(), resource);
        int filled = essenceFluid == null ? 0 : essenceFluid.amount;
    	if(doFill && !MinecraftHelpers.isClientSide()) {
            NetworkHelper.getSoulNetwork(uuid).setCurrentEssence(essence + filled);
    	}
    	return filled;
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
    	String uuid = getOwnerUUID(container);
    	if(uuid == null || uuid.isEmpty()) {
    		return null;
    	}
    	int essence = getCurrentEssence(uuid);
    	FluidStack toDrain = new FluidStack(Blood.getInstance(), maxDrain);
    	FluidStack toDrainEssence = BloodFluidConverter.getInstance().convertReverse(BloodMagicAPI.getLifeEssence(), toDrain);
    	int drainEssence = Math.min(essence, toDrainEssence == null ? 0 : toDrainEssence.amount);
    	if(doDrain && !MinecraftHelpers.isClientSide()) {
            NetworkHelper.getSoulNetwork(uuid).setCurrentEssence(essence - drainEssence);
    	}
    	FluidStack drainedEssence = new FluidStack(BloodMagicAPI.getLifeEssence(), drainEssence);
    	return BloodFluidConverter.getInstance().convert(drainedEssence);
    }

}
