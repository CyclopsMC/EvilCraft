package evilcraft.event;

import evilcraft.Reference;
import evilcraft.core.config.ConfigHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
