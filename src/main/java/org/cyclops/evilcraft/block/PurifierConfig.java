package org.cyclops.evilcraft.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityPurifier;
import org.cyclops.evilcraft.tileentity.TilePurifier;

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
