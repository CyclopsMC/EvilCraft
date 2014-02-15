package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.items.DarkGem;

/**
 * A storage block for {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkBlock extends ConfigurableBlockConnectedTexture {
    
    private static DarkBlock _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkBlock getInstance() {
        return _instance;
    }

    private DarkBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(Block.soundMetalFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2); // Iron tier
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return DarkBlockConfig._instance.ID;
    }
    
    @Override
    public boolean hasSeperateInventoryBlockIcon() {
        return true;
    }

}
