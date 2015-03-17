package evilcraft.block;

import evilcraft.Reference;
import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

/**
 * Config for the Undead Plank.
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
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlock) new ConfigurableBlock(this, Material.wood).
                setHardness(2.0F).setStepSound(Block.soundTypeWood);
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
    	Blocks.fire.func_180686_a(getBlockInstance(), 5, 20);
    }
    
}
