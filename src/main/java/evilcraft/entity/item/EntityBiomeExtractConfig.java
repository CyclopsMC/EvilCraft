package evilcraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.client.render.RenderThrowable;
import evilcraft.core.config.extendedconfig.EntityConfig;
import evilcraft.item.BiomeExtract;
import net.minecraft.client.renderer.entity.Render;

/**
 * Config for the {@link EntityWeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityBiomeExtractConfig extends EntityConfig {

    /**
     * The unique instance.
     */
    public static EntityBiomeExtractConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityBiomeExtractConfig() {
        super(
        	true,
            "entityBiomeExtract",
            null,
            EntityBiomeExtract.class
        );
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderThrowable(BiomeExtract.getInstance());
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }
}
