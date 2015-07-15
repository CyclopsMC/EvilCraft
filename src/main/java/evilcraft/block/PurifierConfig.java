package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.client.render.tileentity.RenderTileEntityPurifier;
import evilcraft.tileentity.TilePurifier;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * Config for the {@link Purifier}.
 * @author rubensworks
 *
 */
public class PurifierConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static PurifierConfig _instance;

    /**
     * Make a new instance.
     */
    public PurifierConfig() {
        super(
                EvilCraft._instance,
        	true,
            "purifier",
            null,
            Purifier.class
        );
    }
    
    @Override
    public void onRegistered() {
        if(MinecraftHelpers.isClientSide()) {
            getMod().getProxy().registerRenderer(TilePurifier.class, new RenderTileEntityPurifier());
        }
    }
    
}
