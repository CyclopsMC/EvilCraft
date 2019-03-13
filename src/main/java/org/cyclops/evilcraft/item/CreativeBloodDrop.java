package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.BloodStainedBlockConfig;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
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
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getTranslationKey() + ".info.autoSupply");
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	updateAutoFill(itemStack, world, entity);
        super.onUpdate(itemStack, world, entity, par4, par5);
    }
    
    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param itemStack The item stack to fill from.
     * @param world The world.
     * @param entity The entity that holds this item.
     */
    public static void updateAutoFill(ItemStack itemStack, World world, Entity entity) {
        IFluidHandler source = FluidUtil.getFluidHandler(itemStack);
    	if(source != null && entity instanceof EntityPlayer && !world.isRemote && ItemHelpers.isActivated(itemStack)) {
            FluidStack tickFluid = source.drain(Integer.MAX_VALUE, false);
            if(tickFluid != null && tickFluid.amount > 0) {
                EntityPlayer player = (EntityPlayer) entity;
                for(EnumHand hand : EnumHand.values()) {
                    ItemStack held = player.getHeldItem(hand);
                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(held);
                    if (!held.isEmpty() && held != itemStack && fluidHandler != null && player.getItemInUseCount() == 0) {
                        if (fluidHandler.fill(tickFluid, false) > 0) {
                            int filled = fluidHandler.fill(new FluidStack(tickFluid.getFluid(), MB_FILL_PERTICK), true);
                            source.drain(filled, true);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
    	list.add(new ItemStack(this));
    }
    
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(blockPos).getBlock();
        if(player.isSneaking()) {
	        if(Configs.isEnabled(BloodStainedBlockConfig.class)
	        		&& (BloodStainedBlock.getInstance().canSetInnerBlock(world.getBlockState(blockPos), block, world, blockPos) || block == BloodStainedBlock.getInstance())) {
	        	BloodStainedBlock.getInstance().stainBlock(world, blockPos, Fluid.BUCKET_VOLUME);
		        if(world.isRemote) {
		        	ParticleBloodSplash.spawnParticles(world, blockPos.add(0, 1, 0), 5, 1 + world.rand.nextInt(2));
		        }
		        return EnumActionResult.PASS;
	        }
	    }
        return super.onItemUseFirst(player, world, blockPos, side, hitX, hitY, hitZ, hand);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!player.isSneaking()) {
            return super.onItemRightClick(world, player, hand);
        } else {
        	RayTraceResult target = this.rayTrace(world, player, false);
        	if(target == null || target.typeOfHit == Type.MISS) {
        		if(!world.isRemote) {
		            ItemHelpers.toggleActivation(itemStack);
		    	}
        	}
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemCapacity(stack, MB_FILL_PERTICK) {
            @Override
            public FluidStack getFluid() {
                return new FluidStack(CreativeBloodDrop.this.getFluid(), MB_FILL_PERTICK / 2);
            }

            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                return new FluidStack(getFluid(), maxDrain);
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if(resource == null) {
                    return 0;
                } else {
                    return resource.amount;
                }
            }
        };
    }
}
