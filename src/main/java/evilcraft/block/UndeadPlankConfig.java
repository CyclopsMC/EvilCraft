package evilcraft.block;

import net.minecraft.init.Blocks;
import evilcraft.Reference;
import evilcraft.core.config.BlockConfig;

/**
 * Config for the {@link UndeadPlank}.
 * @author rubensworks
 *
 */
public class UndeadPlankConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadPlankConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadPlankConfig() {
        super(
        	true,
            "undeadPlank",
            null,
            UndeadPlank.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODPLANK;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
    @Override
    public void onRegistered() {
    	Blocks.fire.setFireInfo(UndeadPlank.getInstance(), 5, 20);
    }
    
}
