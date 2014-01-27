package evilcraft.blocks;
import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.blockcomponents.EntityDropParticleFXBlockComponent;
import evilcraft.api.blockcomponents.IEntityDropParticleFXBlock;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockLog;
import evilcraft.render.particle.ExtendedEntityDropParticleFX;

public class UndeadLog extends ConfigurableBlockLog {
    
    private static UndeadLog _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new UndeadLog(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static UndeadLog getInstance() {
        return _instance;
    }

    private UndeadLog(ExtendedConfig eConfig) {
        super(eConfig);
        setHardness(2.0F);
        setStepSound(soundWoodFootstep);
    }

}
