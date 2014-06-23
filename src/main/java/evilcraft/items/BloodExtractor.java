package evilcraft.items;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.api.HotbarIterator;
import evilcraft.api.IInformationProvider;
import evilcraft.api.ItemHelpers;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.fluids.Blood;
import evilcraft.render.particle.EntityBloodSplashFX;

/**
 * Can extract blood from attacking mobs and {@link BloodStainedBlock}.
 * @author rubensworks
 *
 */
public class BloodExtractor extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodExtractor _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodExtractor(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodExtractor getInstance() {
        return _instance;
    }

    private BloodExtractor(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, BloodExtractorConfig.containerSize, Blood.getInstance());
        setPlaceFluids(true);
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        if(player.isSneaking()) {
	        if(Configs.isEnabled(BloodStainedBlockConfig.class) && block == BloodStainedBlock.getInstance()) {
	            Random random = world.rand;
	            
	            // Fill the extractor a bit
	            int filled = fillBloodExtractor(itemStack, BloodExtractorConfig.minMB, BloodExtractorConfig.maxMB, !world.isRemote);
	            
	            // Transform bloody dirt into regular dirt if we used some of the blood
	            if(filled > 0) {
	                int metaData = world.getBlockMetadata(x, y, z);
	                world.setBlock(x, y, z, BloodStainedBlock.getInstance().getBlockFromMetadata(metaData));
	                
	                if (world.isRemote) {
	                    // Init particles
	                    EntityBloodSplashFX.spawnParticles(world, x, y + 1, z, 5, 1 + random.nextInt(2));
	                }
	                return false;
	            }
	            return false;
	        } else {
	        	if(!world.isRemote) {
	                ItemHelpers.toggleActivation(itemStack);
	        	}
	        }
        }
        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
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
        list.add(IInformationProvider.INFO_PREFIX + L10NHelpers.getLocalizedInfo(this, ".main"));
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
        		getUnlocalizedName() + ".info.autoSupply");
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!player.isSneaking()) {
            return super.onItemRightClick(itemStack, world, player);
        }
        return itemStack;
    }
    
    /**
     * Fill a given Blood Extractor with a random amount of blood.
     * @param itemStack The ItemStack that is a Blood Extractor to fill.
     * @param minimumMB The minimum amount to fill. (inclusive)
     * @param maximumMB The maximum amount to fill. (exclusive)
     * @param doFill If the container really has to be filled, otherwise just simulated.
     * @return The amount of blood that was filled with.
     */
    public int fillBloodExtractor(ItemStack itemStack, int minimumMB, int maximumMB, boolean doFill) {
        int toFill = minimumMB + itemRand.nextInt(maximumMB - minimumMB);
        ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
        int filled = container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), doFill);
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
        int toFill = minimumMB + itemRand.nextInt(maximumMB - minimumMB);
        HotbarIterator it = new HotbarIterator(player);
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
    	BloodContainer.updateAutoFill(this, itemStack, world, entity);
        super.onUpdate(itemStack, world, entity, par4, par5);
    }

}
