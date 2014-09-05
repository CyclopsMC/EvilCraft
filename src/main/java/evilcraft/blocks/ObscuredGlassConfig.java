package evilcraft.blocks;

import evilcraft.core.config.BlockConfig;

/**
 * Config for {@link ObscuredGlass}.
 * @author rubensworks
 *
 */
public class ObscuredGlassConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static ObscuredGlassConfig _instance;

    /**
     * Make a new instance.
     */
    public ObscuredGlassConfig() {
        super(
        	true,
            "obscuredGlass",
            null,
            ObscuredGlass.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return "materialGlass";
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
