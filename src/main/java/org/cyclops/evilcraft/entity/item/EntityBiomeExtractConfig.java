package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.client.render.RenderThrowable;
import org.cyclops.evilcraft.item.BiomeExtract;
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
                EvilCraft._instance,
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

    @Override
    protected Render getRender(RenderManager renderManager, RenderItem renderItem) {
        return null;
    }
}
