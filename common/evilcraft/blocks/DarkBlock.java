package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockConnectedTexture;

public class DarkBlock extends ConfigurableBlockConnectedTexture {
    
    private static DarkBlock _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new DarkBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static DarkBlock getInstance() {
        return _instance;
    }

    private DarkBlock(ExtendedConfig eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(Block.soundMetalFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2); // Iron tier
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return DarkBlockConfig._instance.ID;
    }
    
    @Override
    public boolean hasSeperateInventoryBlockIcon() {
        return true;
    }

}
