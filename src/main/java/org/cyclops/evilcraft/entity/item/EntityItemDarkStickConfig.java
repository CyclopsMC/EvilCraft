package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "entityItemDarkStick",
            null,
            EntityItemDarkStick.class
        );
    }

    @Override
    protected Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return null;
    }
}
