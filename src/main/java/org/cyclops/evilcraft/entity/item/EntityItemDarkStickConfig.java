package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderDarkStick;

/**
 * Config for the {@link EntityItemDarkStick}.
 * @author rubensworks
 *
 */
public class EntityItemDarkStickConfig extends EntityConfig<EntityItemDarkStick> {
    
    /**
     * The unique instance.
     */
    public static EntityItemDarkStickConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemDarkStickConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityItemDarkStick",
            null,
            EntityItemDarkStick.class
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Render<EntityItemDarkStick> getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderDarkStick(renderManager, this);
    }
}
