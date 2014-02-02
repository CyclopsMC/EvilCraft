package evilcraft.entities.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import evilcraft.Reference;
import evilcraft.api.config.EntityConfig;
import evilcraft.items.BloodPearlOfTeleportation;
import evilcraft.proxies.ClientProxy;

public class EntityBloodPearlConfig extends EntityConfig {
    
    public static EntityBloodPearlConfig _instance;

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
