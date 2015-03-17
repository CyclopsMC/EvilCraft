package evilcraft.block;

import evilcraft.api.RegistryManager;
import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipeRegistry;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;
import evilcraft.client.render.block.RenderEnvironmentalAccumulator;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.tileentity.TileEnvironmentalAccumulator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Block that can collect the weather and stuff.
 * @author immortaleeb
 *
 */
public class EnvironmentalAccumulator
        extends ConfigurableBlockContainer
        implements IMachine<EnvironmentalAccumulator, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> {
	
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
	public boolean isNormalCube() {
	    return false;
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockStatedata, EntityPlayer player) {
	    // Environmental Accumulators should not drop upon breaking
	    world.setBlockToAir(blockPos);
	}

    @Override
    public IRecipeRegistry<EnvironmentalAccumulator, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> getRecipeRegistry() {
        return RegistryManager.getRegistry(ISuperRecipeRegistry.class).getRecipeRegistry(this);
    }
}
