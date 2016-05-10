package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.BloodStainedBlockConfig;
import org.cyclops.evilcraft.client.particle.EntityBloodSplashFX;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.tileentity.TileBloodStainedBlock;

import java.util.List;
import java.util.Random;

/**
 * Can extract blood from attacking mobs and {@link BloodStainedBlock}.
 * @author rubensworks
 *
 */
public class BloodExtractor extends ConfigurableDamageIndicatedItemFluidContainer {

    private static final String NBT_TAG_CAPACITY = "tankCapacity";
    
    private static BloodExtractor _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodExtractor getInstance() {
        return _instance;
    }

    public BloodExtractor(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, BloodExtractorConfig.containerSize, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
    	Block block = world.getBlockState(blockPos).getBlock();
        if(player.isSneaking()) {
	        if(Configs.isEnabled(BloodStainedBlockConfig.class) && block == BloodStainedBlock.getInstance()) {
	            Random random = world.rand;
	            
	            // Fill the extractor a bit
	            int amount = 0;
				try {
					amount = ((TileBloodStainedBlock) BloodStainedBlock.getInstance().getTile(world, blockPos)).getAmount();
				} catch (ConfigurableBlockWithInnerBlocksExtended.InvalidInnerBlocksTileException e) {
					e.printStackTrace();
				}
	            int filled = fillBloodExtractor(itemStack, amount, !world.isRemote);
	            BloodStainedBlock.getInstance().unstainBlock(world, blockPos, filled);

	            // Transform bloody dirt into regular dirt if we used some of the blood
	            if(filled > 0 && world.isRemote) {
	                // Init particles
	                EntityBloodSplashFX.spawnParticles(world, blockPos.add(0, 1, 1), 5, 1 + random.nextInt(2));
	            }
	            return EnumActionResult.PASS;
	        }
        }
        return super.onItemUseFirst(itemStack, player, world, blockPos, side, hitX, hitY, hitZ, hand);
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(!player.isSneaking()) {
            return super.onItemRightClick(itemStack, world, player, hand);
        } else {
        	RayTraceResult target = this.getMovingObjectPositionFromPlayer(world, player, false);
        	if(target == null || target.typeOfHit == RayTraceResult.Type.MISS) {
        		if(!world.isRemote) {
		            ItemHelpers.toggleActivation(itemStack);
		    	}
        	}
        }
        return MinecraftHelpers.successAction(itemStack);
    }
    
    /**
     * Fill a given Blood Extractor with a given amount of blood.
     * @param itemStack The ItemStack that is a Blood Extractor to fill.
     * @param amount The amount to fill.
     * @param doFill If the container really has to be filled, otherwise just simulated.
     * @return The amount of blood that was filled with.
     */
    public int fillBloodExtractor(ItemStack itemStack, int amount, boolean doFill) {
        ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
        int filled = container.fill(itemStack, new FluidStack(Blood.getInstance(), amount), doFill);
        return filled;
    }
    
    /**
     * Fill all the Blood Extractors on a player's hotbar for a given fluid amount.
     * It will fill Blood Extractors until the predefined blood amount is depleted.
     * (It fills on at a time).
     * @param player The player to the the Blood Extractors for.
     * @param minimumMB The minimum amount to fill. (inclusive)
     * @param maximumMB The maximum amount to fill. (exclusive)
     */
    public void fillForAllBloodExtractors(EntityPlayer player, int minimumMB, int maximumMB) {
        int toFill = minimumMB + itemRand.nextInt(Math.max(1, maximumMB - minimumMB));
        PlayerInventoryIterator it = new PlayerInventoryIterator(player);
        while(it.hasNext() && toFill > 0) {
            ItemStack itemStack = it.next();
            if(itemStack != null && itemStack.getItem() == BloodExtractor.getInstance()) {
                ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
                toFill -= container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), true);
            }
        }
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	if(ItemHelpers.isActivated(itemStack)) {
    		ItemHelpers.updateAutoFill(this, itemStack, world, entity);
    	}
        super.onUpdate(itemStack, world, entity, par4, par5);
    }

    public void setCapacity(ItemStack itemStack, int capacity) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if(tag == null) {
            tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
        }
        tag.setInteger(NBT_TAG_CAPACITY, capacity);
    }

    @Override
    public int getCapacity(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if(tag == null || !tag.hasKey(NBT_TAG_CAPACITY)) {
            return super.getCapacity(itemStack);
        }
        return tag.getInteger(NBT_TAG_CAPACITY);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

}
