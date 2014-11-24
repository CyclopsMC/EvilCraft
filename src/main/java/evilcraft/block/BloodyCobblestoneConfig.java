package evilcraft.block;

import evilcraft.Reference;
import evilcraft.core.config.configurable.ConfigurableBlock;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.BlockConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Config for the Bloody Cobblestone.
 * @author rubensworks
 *
 */
public class BloodyCobblestoneConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static BloodyCobblestoneConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodyCobblestoneConfig() {
        super(
        	true,
            "bloodyCobblestone",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return (ConfigurableBlock) new ConfigurableBlock(this, Material.rock).
                setHarvestLevelDefined("pickaxe", 0).setHardness(1.5F).setResistance(10.0F).
                setStepSound(Block.soundTypeStone);
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_BLOCKSTONE;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
