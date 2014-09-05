package evilcraft.events;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Reference;
import evilcraft.core.config.ConfigHandler;

/**
 * Event hook for {@link ConfigChangedEvent}.
 * @author rubensworks
 *
 */
public class ConfigChangedEventHook {
    
    /**
     * Update the configurables when the options are changed from the config gui.
     * @param eventArgs The Forge event required for this.
     */
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(Reference.MOD_ID)) {
        	ConfigHandler.getInstance().syncProcessedConfigs();
        }
    }
}
