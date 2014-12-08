package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.Size;
import evilcraft.core.algorithm.Wrapper;
import evilcraft.core.block.CubeDetector;
import evilcraft.core.block.CubeDetector.IDetectionListener;
import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.item.DarkGem;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
    	TileSpiritFurnace.detector.detect(world, new Location(new int[]{x, y, z}), valid, true);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	triggerDetector(world, x, y, z, true);
        world.func_147453_f(x, y, z, this);
    }
    
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
    	if(meta == 1) triggerDetector(world, x, y, z, false);
    	super.onBlockPreDestroy(world, x, y, z, meta);
    }
    
    @Override
	public void onDetect(World world, ILocation location, Size size, boolean valid) {
		Block block = LocationHelpers.getBlock(world, location);
		if(block == this) {
			TileSpiritFurnace.detectStructure(world, location, size, valid);
		}
	}

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side,
                                    float posX, float posY, float posZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if(meta == 1) {
            final Wrapper<ILocation> tileLocationWrapper = new Wrapper<ILocation>();
            TileSpiritFurnace.detector.detect(world, new Location(new int[]{x, y, z}), true, new CubeDetector.IValidationAction() {

                @Override
                public void onValidate(ILocation location, Block block) {
                    if(block == SpiritFurnace.getInstance()) {
                        tileLocationWrapper.set(location);
                    }
                }

            }, false);
            ILocation tileLocation = tileLocationWrapper.get();
            if(tileLocation != null) {
                int[] c = tileLocation.getCoordinates();
                LocationHelpers.getBlock(world,
                        tileLocation).onBlockActivated(world, c[0], c[1], c[2], player, side, posX, posY, posZ);
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, side, posX, posY, posZ);
    }

}
