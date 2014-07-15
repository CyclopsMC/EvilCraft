package evilcraft.blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import evilcraft.api.algorithms.Location;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;
import evilcraft.entities.tileentities.TileSpiritFurnace;
import evilcraft.items.DarkGem;

/**
 * Ore that drops {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkBloodBrick extends ConfigurableBlock {
    
    private static DarkBloodBrick _instance = null;
    
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
        this.setHardness(15.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }
    
    @Override
    public boolean canCreatureSpawn(EnumCreatureType creatureType, IBlockAccess world, int x, int y, int z) {
    	return false;
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	TileSpiritFurnace.detector.detect(world, new Location(new int[]{x, y, z}));
    }

}
