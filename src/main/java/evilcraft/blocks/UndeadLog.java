package evilcraft.blocks;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.configurable.ConfigurableBlockLog;

/**
 * Logs for the Undead Tree.
 * @author rubensworks
 *
 */
public class UndeadLog extends ConfigurableBlockLog {
    
    private static UndeadLog _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new UndeadLog(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static UndeadLog getInstance() {
        return _instance;
    }

    private UndeadLog(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig);
        setHardness(2.0F);
        setStepSound(soundTypeWood);
    }

}
