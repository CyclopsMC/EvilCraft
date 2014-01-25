package evilcraft.api.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import evilcraft.api.render.ModelRender;
import evilcraft.proxies.ClientProxy;

public abstract class EntityConfig extends ExtendedConfig<EntityConfig>{

    public EntityConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Entity> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    @Override
    public void onRegistered() {
        Class<? extends Entity> clazz = (Class<? extends Entity>) this.ELEMENT;
        ClientProxy.ENTITY_RENDERERS.put(clazz, getRender());
    }
    
    /**
     * The range at which MC will send tracking updates.
     * @return
     */
    public int getTrackingRange() {
        return 160;
    }
    
    /**
     * The frequency of tracking updates.
     * @return
     */
    public int getUpdateFrequency() {
        return 10;
    }
    
    /**
     * Whether to send velocity information packets as well.
     * @return
     */
    public boolean sendVelocityUpdates() {
        return false;
    }
    
    protected abstract Render getRender();
}
