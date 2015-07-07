package evilcraft.entity.item;

import evilcraft.EvilCraft;
import evilcraft.item.RedstoneGrenade;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;

/**
 * Config for the {@link EntityRedstoneGrenade}.
 * @author rubensworks
 *
 */
public class EntityRedstoneGrenadeConfig extends EntityConfig {
    
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
    public Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderSnowball(renderManager, RedstoneGrenade.getInstance(), renderItem);
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
    
}
