package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGui;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.Gui;
import evilcraft.render.EntityBloodSplashFX;

public class BloodInfuser extends ConfigurableBlockContainerGui {
    
    private static BloodInfuser _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodInfuser(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodInfuser getInstance() {
        return _instance;
    }

    private BloodInfuser(ExtendedConfig eConfig) {
        super(eConfig, Material.ground, TileBloodInfuser.class, Gui.BLOOD_INFUSER);
    }
    
    public int idDropped(int par1, Random random, int zero) {
        return BloodInfuserConfig._instance.ID;
    }

}
