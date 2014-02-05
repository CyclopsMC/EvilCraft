package evilcraft.blocks;
import java.util.Random;

import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

public class BloodyCobblestone extends ConfigurableBlock {
    
    private static BloodyCobblestone _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodyCobblestone(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodyCobblestone getInstance() {
        return _instance;
    }

    private BloodyCobblestone(ExtendedConfig eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundStoneFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0); // Wood tier
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return BloodyCobblestoneConfig._instance.ID;
    }

}
