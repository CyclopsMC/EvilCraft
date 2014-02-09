package evilcraft.entities.item;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;
import evilcraft.items.BloodPearlOfTeleportation;

/**
 * Config for the {@link EntityBloodPearl}.
 * @author rubensworks
 *
 */
public class EntityBloodPearlConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityBloodPearlConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityBloodPearlConfig() {
        super(
            Reference.ENTITY_BLOODPEARL,
            "Blood Pearl of Teleportation",
            "entityBloodPearl",
            null,
            EntityBloodPearl.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderSnowball(BloodPearlOfTeleportation.getInstance());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
