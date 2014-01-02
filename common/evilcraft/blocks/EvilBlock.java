package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.api.config.ConfigurableBlockConnectedTexture;
import evilcraft.api.config.ExtendedConfig;

public class EvilBlock extends ConfigurableBlockConnectedTexture {
    
    private static EvilBlock _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new EvilBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static EvilBlock getInstance() {
        return _instance;
    }

    private EvilBlock(ExtendedConfig eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundMetalFootstep);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return EvilBlockConfig._instance.ID;
    }

}
