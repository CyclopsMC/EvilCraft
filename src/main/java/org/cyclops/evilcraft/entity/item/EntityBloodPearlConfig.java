package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;
import org.cyclops.evilcraft.item.BloodPearlOfTeleportation;

/**
 * Config for the {@link EntityBloodPearl}.
 * @author rubensworks
 *
 */
public class EntityBloodPearlConfig extends EntityConfig<EntityThrowable> {
    
    /**
     * The unique instance.
     */
    public static EntityBloodPearlConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityBloodPearlConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityBloodPearl",
            null,
            EntityBloodPearl.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball(renderManager, BloodPearlOfTeleportation.getInstance(), renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
