package evilcraft.block;

import evilcraft.client.render.tileentity.RenderTileEntitySpiritPortal;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileSpiritPortal;

/**
 * Config for the {@link evilcraft.block.SpiritPortal}.
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
				true,
				"spiritPortal",
				null,
				SpiritPortal.class
		);
	}
	
	@Override
	public void onRegistered() {
	    if(MinecraftHelpers.isClientSide()) {
	        ClientProxy.TILE_ENTITY_RENDERERS.put(TileSpiritPortal.class, new RenderTileEntitySpiritPortal());
	    }
	}

}