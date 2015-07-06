package evilcraft.entity.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import evilcraft.item.LightningGrenade;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Config for {@link EntityLightningGrenade}.
 * @author rubensworks
 *
 */
public class EntityLightningGrenadeConfig extends EntityConfig {
    
    /**
     * The unique instance.
     */
    public static EntityLightningGrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityLightningGrenadeConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityLightningGrenade",
            null,
            EntityLightningGrenade.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball(renderManager, LightningGrenade.getInstance(), renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
