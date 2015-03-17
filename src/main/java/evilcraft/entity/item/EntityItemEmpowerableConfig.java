package evilcraft.entity.item;

import evilcraft.core.config.extendedconfig.EntityConfig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;

/**
 * Config for the {@link EntityItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerableConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityItemEmpowerableConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemEmpowerableConfig() {
        super(
        	true,
            "entityItemEmpowerable",
            null,
            EntityItemEmpowerable.class
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
