package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.render.RenderThrowable;
import evilcraft.items.Grenade;

/**
 * Configuration for {@link EntityGrenade}.
 * @author immortaleeb
 *
 */
public class EntityGrenadeConfig extends EntityConfig {
    /**
     * The unique instance.
     */
    public static EntityGrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityGrenadeConfig() {
        super(
            true,
            "entityGrenade",
            null,
            EntityGrenade.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected Render getRender() {
        return new RenderThrowable(Grenade.getInstance());
    }

    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
