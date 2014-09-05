package evilcraft.blocks;

import net.minecraft.init.Blocks;
import evilcraft.Reference;
import evilcraft.core.config.BlockConfig;

/**
 * Config for the {@link UndeadLog}.
 * @author rubensworks
 *
 */
public class UndeadLogConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadLogConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadLogConfig() {
        super(
        	true,
            "undeadLog",
            null,
            UndeadLog.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODLOG;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
    @Override
    public void onRegistered() {
    	Blocks.fire.setFireInfo(UndeadLog.getInstance(), 5, 20);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
