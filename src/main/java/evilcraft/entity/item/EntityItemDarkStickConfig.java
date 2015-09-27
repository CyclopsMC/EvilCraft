package evilcraft.entity.item;

import evilcraft.core.config.extendedconfig.EntityConfig;
import net.minecraft.client.renderer.entity.Render;

/**
 * Config for the {@link EntityItemDarkStick}.
 * @author rubensworks
 *
 */
public class EntityItemDarkStickConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityItemDarkStickConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemDarkStickConfig() {
        super(
        	true,
            "entityItemDarkStick",
            null,
            EntityItemDarkStick.class
        );
    }

    @Override
    protected Render getRender() {
        return null;
    }
}
