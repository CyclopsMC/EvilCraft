package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

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
                EvilCraft._instance,
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
    	Blocks.fire.setFireInfo(getBlockInstance(), 5, 20);
    }
    
}
