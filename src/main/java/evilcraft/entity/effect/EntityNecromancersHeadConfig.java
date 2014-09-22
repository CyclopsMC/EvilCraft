package evilcraft.entity.effect;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Items;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.extendedconfig.EntityConfig;

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
    public Render getRender() {
        return new RenderSnowball(Items.skull);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
