package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.BloodStainedBlockConfig;
import org.cyclops.evilcraft.client.particle.EntityBloodSplashFX;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.fluid.Blood;

import java.util.List;

/**
 * Containers that holds an infinite amount of blood.
 * @author rubensworks
 *
 */
public class CreativeBloodDrop extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static CreativeBloodDrop _instance = null;
    
    private static final int MB_FILL_PERTICK = 1000;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static CreativeBloodDrop getInstance() {
        return _instance;
    }

    public CreativeBloodDrop(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, MB_FILL_PERTICK, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    public int getCapacity(ItemStack container) {
        return MB_FILL_PERTICK;
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
                for(EnumHand hand : EnumHand.values()) {
                    ItemStack held = player.getHeldItem(hand);
                    if (held != null && held != itemStack && held.getItem() instanceof IFluidContainerItem && player.getItemInUseCount() == 0) {
                        IFluidContainerItem fluidContainer = (IFluidContainerItem) held.getItem();
                        FluidStack heldFluid = fluidContainer.getFluid(held);
                        if (/*tickFluid.amount >= MB_FILL_PERTICK Not required for creative mode filling
                            && */(heldFluid == null || (heldFluid.isFluidEqual(tickFluid)
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
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(new ItemStack(this));
    }
    
    @Override
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(blockPos).getBlock();
        if(player.isSneaking()) {
	        if(Configs.isEnabled(BloodStainedBlockConfig.class)
	        		&& (BloodStainedBlock.getInstance().canSetInnerBlock(world.getBlockState(blockPos), block, world, blockPos) || block == BloodStainedBlock.getInstance())) {
	        	BloodStainedBlock.getInstance().stainBlock(world, blockPos, FluidContainerRegistry.BUCKET_VOLUME);
		        if(world.isRemote) {
		        	EntityBloodSplashFX.spawnParticles(world, blockPos.add(0, 1, 0), 5, 1 + world.rand.nextInt(2));
		        }
		        return EnumActionResult.PASS;
	        }
	    }
        return super.onItemUseFirst(itemStack, player, world, blockPos, side, hitX, hitY, hitZ, hand);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(!player.isSneaking()) {
            return super.onItemRightClick(itemStack, world, player, hand);
        } else {
        	RayTraceResult target = this.getMovingObjectPositionFromPlayer(world, player, false);
        	if(target == null || target.typeOfHit == Type.MISS) {
        		if(!world.isRemote) {
		            ItemHelpers.toggleActivation(itemStack);
		    	}
        	}
        }
        return MinecraftHelpers.successAction(itemStack);
    }

}
