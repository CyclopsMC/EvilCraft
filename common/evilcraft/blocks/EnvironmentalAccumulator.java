package evilcraft.blocks;

import net.minecraft.block.material.Material;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;

public class EnvironmentalAccumulator extends ConfigurableBlockContainer {
	
	private static EnvironmentalAccumulator _instance = null;
	
	public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new EnvironmentalAccumulator(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static EnvironmentalAccumulator getInstance() {
        return _instance;
    }

	public EnvironmentalAccumulator(ExtendedConfig eConfig) {
		super(eConfig, Material.iron, TileEnvironmentalAccumulator.class);
		this.setRotatable(true);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
