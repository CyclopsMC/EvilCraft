package evilcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * {@link ExtendedDamageSource} must be statically entered in this class and
 * they will automatically be registered into the {@link LanguageRegistry} from Forge.
 * @see ExtendedDamageSource
 * @author rubensworks
 *
 */
public class CustomDeathMessageRegistry {
    
    private static Map<String, String> messages = new HashMap<String, String>();
    static {
        messages.put(ExtendedDamageSource.dieWithoutAnyReason.getID(), "%1$s dies without any apparent reason!");
    }
    
    /**
     * Register all the {@link ExtendedDamageSource} that were statically defined here.
     */
    public static void register() {
        for(Entry<String, String> entry : messages.entrySet())
            LanguageRegistry.instance().addStringLocalization(entry.getKey(), entry.getValue());
    }
    
}
