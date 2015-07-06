package evilcraft.entity.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;

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
	protected Render getRender(RenderManager renderManager, RenderItem renderItem) {
		return null;
	}
}
