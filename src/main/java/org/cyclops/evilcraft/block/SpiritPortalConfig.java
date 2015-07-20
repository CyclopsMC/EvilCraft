package org.cyclops.evilcraft.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntitySpiritPortal;
import org.cyclops.evilcraft.tileentity.TileSpiritPortal;

/**
 * Config for the {@link SpiritPortal}.
 * @author rubensworks
 *
 */
public class SpiritPortalConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
	public static SpiritPortalConfig _instance;

	/**
     * Make a new instance.
     */
	public SpiritPortalConfig() {
		super(
				EvilCraft._instance,
				true,
				"spiritPortal",
				null,
				SpiritPortal.class
		);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(TileSpiritPortal.class, new RenderTileEntitySpiritPortal());
	}

}