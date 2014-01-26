package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.tileentity.TileEntityBeaconRenderer;

public class EnvironmentalAccumulatorConfig extends BlockConfig {

	public static EnvironmentalAccumulatorConfig _instance;
	
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
		ClientProxy.TILE_ENTITY_RENDERERS.put(TileEnvironmentalAccumulator.class, new TileEntityBeaconRenderer());
	}

}