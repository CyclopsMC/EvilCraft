package evilcraft.block;

import evilcraft.Reference;
import evilcraft.core.config.BlockConfig;

/**
 * Config for the {@link BloodyCobblestone}.
 * @author rubensworks
 *
 */
public class BloodyCobblestoneConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static BloodyCobblestoneConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodyCobblestoneConfig() {
        super(
        	true,
            "bloodyCobblestone",
            null,
            BloodyCobblestone.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_BLOCKSTONE;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
