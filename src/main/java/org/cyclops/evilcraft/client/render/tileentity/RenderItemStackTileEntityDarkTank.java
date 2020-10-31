package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.render.tileentity.ItemStackTileEntityRendererBase;
import org.cyclops.evilcraft.tileentity.TileDarkTank;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

/**
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemStackTileEntityDarkTank extends ItemStackTileEntityRendererBase {

    public RenderItemStackTileEntityDarkTank() {
        super(TileDarkTank::new);
    }

}
