package evilcraft.blocks;
import net.minecraft.block.material.Material;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockSapling;

public class UndeadSapling extends ConfigurableBlockSapling {
    
    private static UndeadSapling _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new UndeadSapling(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static UndeadSapling getInstance() {
        return _instance;
    }

    private UndeadSapling(ExtendedConfig eConfig) {
        super(eConfig, Material.plants);
    }

}
