package evilcraft.entities.block;

import evilcraft.Reference;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.LightningBomb;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.RenderBombPrimed;

public class EntityLightningBombPrimedConfig extends ExtendedConfig {
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_ENTITY, comment = "The amount of ticks (on average), this bomb should tick before explosion.")
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
    
    @Override
    public void onRegistered() {
        ClientProxy.ENTITY_RENDERERS.put(EntityLightningBombPrimed.class, new RenderBombPrimed(LightningBomb.getInstance()));
    }
    
}
