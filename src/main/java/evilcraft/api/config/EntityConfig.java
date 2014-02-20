package evilcraft.api.config;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.proxies.ClientProxy;

/**
 * Config for entities.
 * For mobs, there is the {@link MobConfig}.
 * For entities with custom models there is {@link ModelEntityConfig}.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EntityConfig extends ExtendedConfig<EntityConfig>{

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public EntityConfig(boolean enabled, String namedId, String comment, Class<? extends Entity> element) {
        super(enabled, namedId, comment, element);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        @SuppressWarnings("unchecked")
        Class<? extends Entity> clazz = (Class<? extends Entity>) this.ELEMENT;
        ClientProxy.ENTITY_RENDERERS.put(clazz, getRender());
    }
    
    /**
     * The range at which MC will send tracking updates.
     * @return The tracking range.
     */
    public int getTrackingRange() {
        return 160;
    }
    
    /**
     * The frequency of tracking updates.
     * @return The update frequency.
     */
    public int getUpdateFrequency() {
        return 10;
    }
    
    /**
     * Whether to send velocity information packets as well.
     * @return Send velocity updates?
     */
    public boolean sendVelocityUpdates() {
        return false;
    }
    
    protected abstract Render getRender();
}
