package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockLog;

/**
 * Config for the Undead Log.
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
                EvilCraft._instance,
        	true,
            "undeadLog",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlockLog) new ConfigurableBlockLog(this).
                setHardness(2.0F).setStepSound(Block.soundTypeWood);
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
    	Blocks.fire.setFireInfo(getBlockInstance(), 5, 20);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
