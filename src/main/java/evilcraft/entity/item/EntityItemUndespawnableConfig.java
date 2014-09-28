package evilcraft.entity.item;

import net.minecraft.client.renderer.entity.Render;
import evilcraft.core.config.extendedconfig.EntityConfig;

/**
 * Config for the {@link EntityItemUndespawnable}.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnableConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityItemUndespawnableConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemUndespawnableConfig() {
        super(
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
	protected Render getRender() {
		return null;
	}
}
