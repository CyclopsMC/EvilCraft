package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.client.render.tileentity.RenderTileEntityPurifier;
import evilcraft.tileentity.TilePurifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;

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
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(TilePurifier.class, new RenderTileEntityPurifier());
    }
    
}
