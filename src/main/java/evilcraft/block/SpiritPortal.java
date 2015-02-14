package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.tileentity.TileSpiritPortal;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Block that can collect the weather and stuff.
 * @author immortaleeb
 *
 */
public class SpiritPortal extends ConfigurableBlockContainer {

	private static SpiritPortal _instance = null;

	/**
     * Initialise the configurable.
     * @param eConfig The config.
     */
	public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SpiritPortal(eConfig);
        else
            eConfig.showDoubleInitError();
    }

	/**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritPortal getInstance() {
        return _instance;
    }

	private SpiritPortal(ExtendedConfig<BlockConfig> eConfig) {
		super(eConfig, Material.iron, TileSpiritPortal.class);
		this.setRotatable(true);
		this.setStepSound(soundTypeCloth);
		this.setHardness(50.0F);
		this.setResistance(6000000.0F);   // Can not be destroyed by explosions
        this.setBlockBounds(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
        this.setLightLevel(0.5F);
        this.setRotatable(false);
	}

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
	    // Portals should not drop upon breaking
	    world.setBlockToAir(x, y, z);
	}
}
