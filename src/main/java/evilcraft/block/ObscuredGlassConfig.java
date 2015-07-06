package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

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
                EvilCraft._instance,
        	true,
            "obscuredGlass",
            null,
            ObscuredGlass.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_BLOCKGLASS;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
