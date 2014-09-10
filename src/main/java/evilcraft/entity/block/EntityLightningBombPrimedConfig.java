package evilcraft.entity.block;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.block.LightningBomb;
import evilcraft.client.render.block.RenderBombPrimed;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.EntityConfig;

/**
 * Config for {@link EntityLightningBombPrimed}.
 * @author rubensworks
 *
 */
public class EntityLightningBombPrimedConfig extends EntityConfig {
    
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
        	true,
            "entityLightningBomb",
            null,
            EntityLightningBombPrimed.class
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderBombPrimed(LightningBomb.getInstance());
    }
    
}
