package evilcraft.entity.effect;

import evilcraft.core.config.extendedconfig.EntityConfig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Config for the {@link EntityNecromancersHead}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHeadConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityNecromancersHeadConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityNecromancersHeadConfig() {
        super(
        	true,
            "entityNecromancersHead",
            null,
            EntityNecromancersHead.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball(renderManager, Items.skull, renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
