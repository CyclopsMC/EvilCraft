package evilcraft.blocks;

import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.entities.tileentities.TilePurifier;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.block.RenderPurifier;
import evilcraft.render.tileentity.TileEntityPurifierItemRenderer;

/**
 * Config for the {@link Purifier}.
 * @author rubensworks
 *
 */
public class PurifierConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static PurifierConfig _instance;

    /**
     * Make a new instance.
     */
    public PurifierConfig() {
        super(
        	true,
            "purifier",
            null,
            Purifier.class
        );
    }
    
    @Override
    public void onRegistered() {
        if(Helpers.isClientSide()) {
            ClientProxy.BLOCK_RENDERERS.add(new RenderPurifier());
            ClientProxy.TILE_ENTITY_RENDERERS.put(TilePurifier.class, new TileEntityPurifierItemRenderer());
        }
    }
    
}
