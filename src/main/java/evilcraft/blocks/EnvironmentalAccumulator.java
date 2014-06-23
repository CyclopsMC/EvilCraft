package evilcraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.L10NHelpers;
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
     * State indicating the environmental accumulator is idle.
     */
    public static final int STATE_IDLE = 0;
    /**
     * State indicating the environmental accumulator is currently
     * processing an item.
     */
    public static final int STATE_PROCESSING_ITEM = 1;
    /**
     * State indicating the environmental accumulator is cooling
     * down.
     */
    public static final int STATE_COOLING_DOWN = 2;
    /**
     * State indicating the environmental accumulator has just
     * finished processing an item. This state is necessary
     * because using this state we can put some delay between
     * processing an item and cooling down so that the client
     * gets a moment to show an effect when an item has finished
     * processing.
     */
    public static final int STATE_FINISHED_PROCESSING_ITEM = 3;
    
    private IIcon sideIcon;
    private IIcon bottomIcon;
    private IIcon topIcon;

	private EnvironmentalAccumulator(ExtendedConfig<BlockConfig> eConfig) {
		super(eConfig, Material.iron, TileEnvironmentalAccumulator.class);
		this.setRotatable(true);
		this.setStepSound(soundTypeMetal);
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
	public IIcon getIcon(int side, int meta) {
	    if (side == ForgeDirection.UP.ordinal())
	        return topIcon;
	    
	    if (side == ForgeDirection.DOWN.ordinal())
	        return bottomIcon;
	    
	    return sideIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
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
		return L10NHelpers.getLocalizedInfo(this);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}
}
