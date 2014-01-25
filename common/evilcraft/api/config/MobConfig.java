package evilcraft.api.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.api.render.ModelRender;
import evilcraft.proxies.ClientProxy;

public abstract class MobConfig extends ExtendedConfig<MobConfig>{

    public MobConfig(int defaultId, String name, String namedId,
            String comment, Class element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    public abstract int getBackgroundEggColor();
    public abstract int getForegroundEggColor();
    
    public abstract Render getRender();

}
