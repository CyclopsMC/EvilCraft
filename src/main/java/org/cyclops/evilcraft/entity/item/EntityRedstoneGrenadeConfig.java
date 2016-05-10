package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.item.RedstoneGrenade;

/**
 * Config for the {@link EntityRedstoneGrenade}.
 * @author rubensworks
 *
 */
public class EntityRedstoneGrenadeConfig extends EntityConfig<EntityRedstoneGrenade> {
    
    /**
     * The unique instance.
     */
    public static EntityRedstoneGrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityRedstoneGrenadeConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityRedstoneGrenade",
            null,
            EntityRedstoneGrenade.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<EntityRedstoneGrenade> getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball<EntityRedstoneGrenade>(renderManager, RedstoneGrenade.getInstance(), renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
