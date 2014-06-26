package evilcraft.blocks;

import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.block.RenderEnvironmentalAccumulator;
import evilcraft.render.tileentity.TileEntityEnvironmentalAccumulatorRenderer;

/**
 * Config for the {@link EnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class EnvironmentalAccumulatorConfig extends BlockConfig {

    /**
     * The unique instance.
     */
	public static EnvironmentalAccumulatorConfig _instance;
	
	/**
	 * The cooldown tick for accumulating the weather.
	 */
	@ConfigurableProperty(category = ElementTypeCategory.GENERAL, isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to cool down")
	public static int defaultTickCooldown = Helpers.MINECRAFT_DAY / 2;
	
	/**
	 * The default number of ticks it takes to process
	 * an item.
	 */
	@ConfigurableProperty(category = ElementTypeCategory.GENERAL, isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to process an item.")
	public static int defaultProcessItemTickCount = 100;
	
	/**
	 * Default speed with which an item will move
	 * when being processed by an environmental accumulator.
	 */
	@ConfigurableProperty(category = ElementTypeCategory.GENERAL, isCommandable = true, comment = "Sets the default default speed in increments per tick with which an item will move when being process by an environmental accumulator.")
	public static double defaultProcessItemSpeed = 0.3d / 20;
	
	/**
     * Make a new instance.
     */
	public EnvironmentalAccumulatorConfig() {
		super(
				true,
				"environmentalAccumulator",
				null,
				EnvironmentalAccumulator.class
		);
	}
	
	@Override
	public void onRegistered() {
	    if(Helpers.isClientSide()) {
	        ClientProxy.BLOCK_RENDERERS.add(new RenderEnvironmentalAccumulator());
	        ClientProxy.TILE_ENTITY_RENDERERS.put(TileEnvironmentalAccumulator.class, new TileEntityEnvironmentalAccumulatorRenderer());
	    }
	}

}