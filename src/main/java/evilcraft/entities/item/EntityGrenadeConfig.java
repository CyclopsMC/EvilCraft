package evilcraft.entities.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.EntityConfig;
import evilcraft.items.Grenade;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;

/**
 * Created by enver on 28/08/14.
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
        return new RenderSnowball(Grenade.getInstance());
    }

    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
