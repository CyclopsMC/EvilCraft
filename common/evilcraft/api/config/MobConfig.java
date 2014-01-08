package evilcraft.api.config;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.proxies.ClientProxy;

public abstract class MobConfig extends ExtendedConfig<MobConfig>{

    public MobConfig(int defaultId, String name, String namedId,
            String comment, Class element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    public abstract int getBackgroundEggColor();
    public abstract int getForegroundEggColor();
    
    @Override
    public void onRegistered() {
        Class<? extends EntityLiving> clazz = (Class<? extends EntityLiving>) this.ELEMENT;
        ClientProxy.ENTITY_RENDERERS.put(clazz, getRender());
        ModLoader.addSpawn(clazz, 1, 0, 1, EnumCreatureType.monster);
    }
    
    public abstract Render getRender();

}
