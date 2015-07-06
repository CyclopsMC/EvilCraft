package evilcraft.block;

import evilcraft.EvilCraft;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for the {@link UndeadLeaves}.
 * @author rubensworks
 *
 */
public class UndeadLeavesConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadLeavesConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadLeavesConfig() {
        super(
                EvilCraft._instance,
        	true,
            "undeadLeaves",
            null,
            UndeadLeaves.class
        );
    }
    
    @Override
    public void onRegistered() {
    	Blocks.fire.setFireInfo(UndeadLeaves.getInstance(), 30, 60);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
