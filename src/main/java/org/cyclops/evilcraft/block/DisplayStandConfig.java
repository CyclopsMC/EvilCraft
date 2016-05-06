package org.cyclops.evilcraft.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityDisplayStand;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

/**
 * Config for the {@link DisplayStand}.
 * @author rubensworks
 *
 */
public class DisplayStandConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static DisplayStandConfig _instance;

    /**
     * Make a new instance.
     */
    public DisplayStandConfig() {
        super(
                EvilCraft._instance,
        	true,
            "displayStand",
            null,
            DisplayStand.class
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        EvilCraft.proxy.registerRenderer(TileDisplayStand.class, new RenderTileEntityDisplayStand());
    }
}
