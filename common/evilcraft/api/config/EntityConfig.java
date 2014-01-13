package evilcraft.api.config;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.blocks.LightningBomb;
import evilcraft.entities.block.EntityLightningBombPrimed;
import evilcraft.items.LightningGrenade;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.RenderBombPrimed;

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
    
    public abstract Render getRender();
}
