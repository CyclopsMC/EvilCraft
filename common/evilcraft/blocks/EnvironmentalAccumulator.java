package evilcraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;
import evilcraft.render.block.RenderEnvironmentalAccumulator;

/**
 * Block that can collect the weather and stuff.
 * @author immortaleeb
 *
 */
public class EnvironmentalAccumulator extends ConfigurableBlockContainer implements IInformationProvider {
	
	private static EnvironmentalAccumulator _instance = null;
	
	/**
     * Initialise the configurable.
     * @param eConfig The config.
     */
	public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new EnvironmentalAccumulator(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
	/**
     * Get the unique instance.
     * @return The instance.
     */
    public static EnvironmentalAccumulator getInstance() {
        return _instance;
    }
    
    /**
     * First bit of metadata indicates the beam is active.
     * 0: beam active, 1: beam inactive
     */
    public static final int BEAM_ACTIVE = 0;
    /**
     * First bit of metadata indicates the beam is active.
     * 0: beam active, 1: beam inactive
     */
    public static final int BEAM_INACTIVE = 1;
    
    /**
     * Second bit indicates wether or not we're moving an item in the beam
     * 0: not moving, 1: moving
     */
    public static final int MOVING_ITEM = 2;
    
    private Icon sideIcon;
    private Icon bottomIcon;
    private Icon topIcon;

	private EnvironmentalAccumulator(ExtendedConfig<BlockConfig> eConfig) {
		super(eConfig, Material.iron, TileEnvironmentalAccumulator.class);
		this.setRotatable(true);
		this.setStepSound(soundMetalFootstep);
		this.setHardness(50.0F);
		this.setResistance(6000000.0F);   // Can not be destroyed by explosions
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
    public int getRenderType() {
        return RenderEnvironmentalAccumulator.ID;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
	    return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta) {
	    if (side == ForgeDirection.UP.ordinal())
	        return topIcon;
	    
	    if (side == ForgeDirection.DOWN.ordinal())
	        return bottomIcon;
	    
	    return sideIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister) {
	    sideIcon = iconRegister.registerIcon(getTextureName() + "_side");
	    bottomIcon = iconRegister.registerIcon(getTextureName() + "_bottom");
	    topIcon = iconRegister.registerIcon(getTextureName() + "_top");
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
	    // Environmental Accumulators should not drop upon breaking
	    world.setBlockToAir(x, y, z);
	}
	
	@Override
    public String getInfo(ItemStack itemStack) {
        return IInformationProvider.INFO_PREFIX + "Found at Dark Temples, high in the mountains.";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}
    
    /**
     * Check if the metadata corresponds to an accumulator with an active beam.
     * @param metadata The metadata of an environmental accumulator.
     * @return If the beam is active.
     */
    public static boolean isBeamActive(int metadata) {
        return (metadata & EnvironmentalAccumulator.BEAM_INACTIVE) == 0;
    }
    
    /**
     * Check if the metadata corresponds to an accumulator with a moving item.
     * @param metadata The metadata of an environmental accumulator.
     * @return If there is a moving item.
     */
    public static boolean isMovingItem(int metadata) {
        return (metadata & EnvironmentalAccumulator.MOVING_ITEM) == EnvironmentalAccumulator.MOVING_ITEM;
    }
    
    /**
     * Check if the metadata corresponds to an accumulator with a completed moving item.
     * @param metadata The metadata of an environmental accumulator.
     * @return If the item moving is done.
     */
    public static boolean isDoneMovingItem(int metadata) {
        return !isBeamActive(metadata) && isMovingItem(metadata);
    }
}
