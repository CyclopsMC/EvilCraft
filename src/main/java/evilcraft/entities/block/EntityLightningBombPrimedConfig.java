package evilcraft.entities.block;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.blocks.LightningBomb;
import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.EntityConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;
import evilcraft.render.block.RenderBombPrimed;

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
    @ConfigurableProperty(category = ElementTypeCategory.ENTITY, comment = "The amount of ticks (on average), this bomb should tick before explosion.")
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
