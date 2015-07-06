package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.core.config.configurable.ConfigurableBlockLog;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

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
