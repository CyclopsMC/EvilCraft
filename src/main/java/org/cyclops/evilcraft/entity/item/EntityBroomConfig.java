package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderBroom;

/**
 * Config for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class EntityBroomConfig extends EntityConfig<EntityBroom> {
    
    /**
     * The unique instance.
     */
    public static EntityBroomConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityBroomConfig() {
        super(
                EvilCraft._instance,
        	true,
            "broomEntity",
            null,
            EntityBroom.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderBroom(renderManager, this);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
    @Override
    public int getUpdateFrequency() {
        return 10;
    }
}
