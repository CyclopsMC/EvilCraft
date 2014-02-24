package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockConnectedTexture;

/**
 * A test block that is only used for testing stuff in development.
 * @author rubensworks
 *
 */
public class EvilBlock extends ConfigurableBlockConnectedTexture {
    
    private static EvilBlock _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new EvilBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EvilBlock getInstance() {
        return _instance;
    }

    private EvilBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundTypeMetal);
        this.setHarvestLevel("pickaxe", 0);
    }
    
    @Override
    public Item getItemDropped(int meta, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @Override
    public boolean isKeepNBTOnDrop() {
		return false;
	}

}
