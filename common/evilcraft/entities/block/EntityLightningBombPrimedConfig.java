package evilcraft.entities.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.EntityConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.render.ModelRender;
import evilcraft.blocks.LightningBomb;
import evilcraft.render.block.RenderBombPrimed;

public class EntityLightningBombPrimedConfig extends EntityConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.ENTITY, comment = "The amount of ticks (on average), this bomb should tick before explosion.")
    public static int fuse = 100;
    
    public static EntityLightningBombPrimedConfig _instance;

    public EntityLightningBombPrimedConfig() {
        super(
            Reference.ENTITY_LIGHTNINGBOMB,
            "Lightning Bomb",
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
