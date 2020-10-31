package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.render.tileentity.ItemStackTileEntityRendererBase;
import org.cyclops.evilcraft.tileentity.TileBloodChest;

/**
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemStackTileEntityBloodChest extends ItemStackTileEntityRendererBase {

    public RenderItemStackTileEntityBloodChest() {
        super(TileBloodChest::new);
    }

}
