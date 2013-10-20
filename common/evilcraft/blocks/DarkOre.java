package evilcraft.blocks;
import java.util.Random;

import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableBlock;
import evilcraft.api.config.ExtendedConfig;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

public class DarkOre extends ConfigurableBlock {
    
    private static DarkOre _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new DarkOre(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static DarkOre getInstance() {
        return _instance;
    }

    private DarkOre(ExtendedConfig eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(20.0F);
        this.setStepSound(Block.soundMetalFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 2); // Iron tier
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return DarkOreConfig._instance.ID;
    }

}
