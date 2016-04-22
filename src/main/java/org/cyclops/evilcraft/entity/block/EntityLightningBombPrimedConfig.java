package org.cyclops.evilcraft.entity.block;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.LightningBomb;
import org.cyclops.evilcraft.client.render.block.RenderBombPrimed;

/**
 * Config for {@link EntityLightningBombPrimed}.
 * @author rubensworks
 *
 */
public class EntityLightningBombPrimedConfig extends EntityConfig<EntityTNTPrimed> {
    
    /**
     * The unique instance.
     */
    public static EntityLightningBombPrimedConfig _instance;
    
    /**
     * The amount of ticks (on average), this bomb should tick before explosion.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ENTITY, comment = "The amount of ticks (on average), this bomb should tick before explosion.")
    public static int fuse = 100;    

    /**
     * Make a new instance.
     */
    public EntityLightningBombPrimedConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entityLightningBomb",
            null,
            EntityLightningBombPrimed.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render<EntityTNTPrimed> getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderBombPrimed(renderManager, LightningBomb.getInstance());
    }
    
}
