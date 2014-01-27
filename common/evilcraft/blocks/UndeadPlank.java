package evilcraft.blocks;
import net.minecraft.block.material.Material;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;

public class UndeadPlank extends ConfigurableBlock {
    
    private static UndeadPlank _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new UndeadPlank(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static UndeadPlank getInstance() {
        return _instance;
    }

    private UndeadPlank(ExtendedConfig eConfig) {
        super(eConfig, Material.wood);
        setHardness(2.0F);
        setStepSound(soundWoodFootstep);
    }

}
