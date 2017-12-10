package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemEmpowerable}.
 * @author rubensworks
 *
 */
public class EntityItemEmpowerableConfig extends EntityConfig<EntityItem> {
    
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
            "entity_item_empowerable",
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
	public Render<EntityItem> getRender(RenderManager renderManager, RenderItem renderItem) {
		return new RenderEntityItem(renderManager, renderItem);
	}
}
