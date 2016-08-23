package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderNull;

/**
 * Config for the {@link EntityItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerableConfig extends EntityConfig<Entity> {
    
    /**
     * The unique instance.
     */
    public static EntityItemEmpowerableConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemEmpowerableConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityItemEmpowerable",
            null,
            EntityItemEmpowerable.class
        );
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }

    @SideOnly(Side.CLIENT)
	@Override
	public Render<Entity> getRender(RenderManager renderManager, RenderItem renderItem) {
		return new RenderNull(renderManager);
	}
}
