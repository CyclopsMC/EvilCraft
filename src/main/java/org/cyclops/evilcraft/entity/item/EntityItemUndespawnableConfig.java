package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemUndespawnable}.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnableConfig extends EntityConfig<Entity> {
    
    /**
     * The unique instance.
     */
    public static EntityItemUndespawnableConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemUndespawnableConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityItemUndespawnable",
            null,
            EntityItemUndespawnable.class
        );
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }

	@Override
	public Render<Entity> getRender(RenderManager renderManager, RenderItem renderItem) {
		return null;
	}
}
