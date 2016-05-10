package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityNecromancersHead}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHeadConfig extends EntityConfig<Entity> {
    
    /**
     * The unique instance.
     */
    public static EntityNecromancersHeadConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityNecromancersHeadConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityNecromancersHead",
            null,
            EntityNecromancersHead.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<Entity> getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball<Entity>(renderManager, Items.skull, renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
