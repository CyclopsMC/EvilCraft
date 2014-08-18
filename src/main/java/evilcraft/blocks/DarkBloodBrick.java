package evilcraft.blocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.algorithms.ILocation;
import evilcraft.api.algorithms.Location;
import evilcraft.api.algorithms.Size;
import evilcraft.api.block.CubeDetector.IDetectionListener;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;
import evilcraft.api.helpers.LocationHelpers;
import evilcraft.entities.tileentities.TileSpiritFurnace;
import evilcraft.items.DarkGem;

/**
 * Ore that drops {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkBloodBrick extends ConfigurableBlock implements IDetectionListener {
    
    private static DarkBloodBrick _instance = null;
    
    private IIcon blockIconInactive;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkBloodBrick(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkBloodBrick getInstance() {
        return _instance;
    }

    private DarkBloodBrick(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    	super.registerBlockIcons(iconRegister);
        blockIconInactive = iconRegister.registerIcon(getTextureName() + "_inactive");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    	if(meta == 0) {
    		return blockIconInactive;
    	}
        return super.getIcon(side, meta);
    }
    
    @Override
    public boolean canCreatureSpawn(EnumCreatureType creatureType, IBlockAccess world, int x, int y, int z) {
    	return false;
    }
    
    private void triggerDetector(World world, int x, int y, int z, boolean valid) {
    	TileSpiritFurnace.detector.detect(world, new Location(new int[]{x, y, z}), valid);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	triggerDetector(world, x, y, z, true);
    }
    
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
    	triggerDetector(world, x, y, z, false);
    	super.onBlockPreDestroy(world, x, y, z, meta);
    }
    
    @Override
	public void onDetect(World world, ILocation location, Size size, boolean valid) {
		Block block = LocationHelpers.getBlock(world, location);
		if(block == this) {
			TileSpiritFurnace.detectStructure(world, location, size, valid);
		}
	}

}
