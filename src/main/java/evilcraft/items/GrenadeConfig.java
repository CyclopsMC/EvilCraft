package evilcraft.items;

import evilcraft.api.config.ItemConfig;
import evilcraft.api.helpers.MinecraftHelpers;
import evilcraft.proxies.ClientProxy;
import evilcraft.render.item.RenderItemGrenade;

/**
 * Config for the {@link Grenade}.
 * @author immortaleeb
 */
public class GrenadeConfig extends ItemConfig {
    /**
     * The unique instance.
     */
    public static GrenadeConfig _instance;

    /**
     * Make a new instance.
     */
    public GrenadeConfig() {
        super(
            true,
            "grenade",
            null,
            Grenade.class
        );
    }
    
    @Override
    public void onRegistered() {
    	super.onRegistered();
    	
    	if (MinecraftHelpers.isClientSide()) {
    		ClientProxy.ITEM_RENDERERS.put(this.getItemInstance(), new RenderItemGrenade());
    	}
    }
}
