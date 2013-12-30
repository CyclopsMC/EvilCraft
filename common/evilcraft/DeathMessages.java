package evilcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class DeathMessages {
    
    private static Map<String, String> messages = new HashMap<String, String>();
    static {
        messages.put(ExtendedDamageSource.dieWithoutAnyReason.getID(), "%1$s sterft zonder enige reden!");
    }

    public static void register() {
        for(Entry<String, String> entry : messages.entrySet())
            LanguageRegistry.instance().addStringLocalization(entry.getKey(), entry.getValue());
    }
    
}
