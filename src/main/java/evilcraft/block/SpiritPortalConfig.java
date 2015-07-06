package evilcraft.block;

import evilcraft.EvilCraft;
import evilcraft.client.render.tileentity.RenderTileEntitySpiritPortal;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileSpiritPortal;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

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
				EvilCraft._instance,
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