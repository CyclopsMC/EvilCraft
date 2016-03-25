package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.recipe.custom.api.IMachine;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.cyclopscore.recipe.custom.api.ISuperRecipeRegistry;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.item.EnvironmentalAccumulationCoreConfig;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;

import java.util.Random;

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

	public EnvironmentalAccumulator(ExtendedConfig<BlockConfig> eConfig) {
		super(eConfig, Material.iron, TileEnvironmentalAccumulator.class);
		this.setRotatable(true);
		this.setStepSound(SoundType.METAL);
		this.setHardness(50.0F);
		this.setResistance(6000000.0F);   // Can not be destroyed by explosions
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState blockState) {
	    return false;
	}

	@Override
	protected void onPreBlockDestroyed(World world, BlockPos blockPos) {
		/*if(!world.isRemote) {
			BlockPos closest = DarkTempleGenerator.getClosestForCoords(world, blockPos.getX(), blockPos.getZ());
			if(closest != null) {
				DarkTempleGenerator.getCachedData(world).addFailedLocation(closest.getX() / WorldHelpers.CHUNK_SIZE, closest.getZ() / WorldHelpers.CHUNK_SIZE);
			}
		}*/
		// TODO: enable when
		super.onPreBlockDestroyed(world, blockPos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return EnvironmentalAccumulationCoreConfig._instance.getItemInstance();
	}

	@Override
    public IRecipeRegistry<EnvironmentalAccumulator, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> getRecipeRegistry() {
        return EvilCraft._instance.getRegistryManager().getRegistry(ISuperRecipeRegistry.class).getRecipeRegistry(this);
    }
}
