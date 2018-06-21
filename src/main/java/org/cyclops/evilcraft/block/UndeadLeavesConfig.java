package org.cyclops.evilcraft.block;

import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
            "undead_leaves",
            null,
            UndeadLeaves.class
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
    	Blocks.FIRE.setFireInfo(UndeadLeaves.getInstance(), 30, 60);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }

    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_LEAVESTREE;
    }
}
