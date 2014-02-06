package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.tileentity.TileEntityBeaconRenderer;
import evilcraft.render.tileentity.TileEntityEnvironmentalAccumulatorRenderer;

public class EnvironmentalAccumulatorConfig extends BlockConfig {

	public static EnvironmentalAccumulatorConfig _instance;
	
	@ConfigurableProperty(category = ElementTypeCategory.GENERAL, isCommandable = true, comment = "Sets the amount of ticks the environmental accumulator takes to cool down")
	public static int tickCooldown = Helpers.MINECRAFT_DAY / 2;
	
	public EnvironmentalAccumulatorConfig() {
		super(
				Reference.BLOCK_ENVIRONMENTAL_ACCUMULATOR, 
				"Environmental Accumulator",
				"environmentalAccumulator",
				null,
				EnvironmentalAccumulator.class
		);
	}
	
	@Override
	public void onRegistered() {
	    if (Helpers.isClientSide())
	        ClientProxy.TILE_ENTITY_RENDERERS.put(TileEnvironmentalAccumulator.class, new TileEntityEnvironmentalAccumulatorRenderer());
	}

}