package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * Config for the {@link EntityAttackVengeanceBeam}.
 * @author rubensworks
 *
 */
public class EntityAttackVengeanceBeamConfig extends EntityConfig<Entity> {

    /**
     * The unique instance.
     */
    public static EntityAttackVengeanceBeamConfig _instance;

    /**
     * If crossed beams should cause explosions.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ENTITY, comment = "If crossed beams should cause explosions.", isCommandable = true)
    public static boolean crossBeamsExplosions = true;

    /**
     * Make a new instance.
     */
    public EntityAttackVengeanceBeamConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityAttackVengeanceBeam",
            null,
            EntityAttackVengeanceBeam.class
        );
    }
    
    @Override
    public boolean isDisableable() {
    	return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<Entity> getRender(RenderManager renderManager, RenderItem renderItem) {
    	return new RenderNull(renderManager);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
