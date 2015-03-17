package evilcraft.item;

import evilcraft.Configs;
import evilcraft.block.BloodStainedBlock;
import evilcraft.block.BloodStainedBlockConfig;
import evilcraft.client.particle.EntityBloodSplashFX;
import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.ItemHelpers;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.fluid.Blood;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlockState(blockPos).getBlock();
        if(player.isSneaking()) {
	        if(Configs.isEnabled(BloodStainedBlockConfig.class)
	        		&& (BloodStainedBlock.getInstance().canSetInnerBlock(block, world, blockPos) || block == BloodStainedBlock.getInstance())) {
	        	BloodStainedBlock.getInstance().stainBlock(world, blockPos, FluidContainerRegistry.BUCKET_VOLUME);
		        if(world.isRemote) {
		        	EntityBloodSplashFX.spawnParticles(world, blockPos.add(0, 1, 0), 5, 1 + world.rand.nextInt(2));
		        }
		        return false;
	        }
	    }
        return super.onItemUseFirst(itemStack, player, world, blockPos, side, hitX, hitY, hitZ);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!player.isSneaking()) {
            return super.onItemRightClick(itemStack, world, player);
        } else {
        	MovingObjectPosition target = this.getMovingObjectPositionFromPlayer(world, player, false);
        	if(target == null || target.typeOfHit == MovingObjectType.MISS) {
        		if(!world.isRemote) {
		            ItemHelpers.toggleActivation(itemStack);
		    	}
        	}
        }
        return itemStack;
    }

}
