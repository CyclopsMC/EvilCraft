package evilcraft.items;

import evilcraft.api.config.ExtendedConfig;
import evilcraft.events.BloodObtainLivingDeathEventHook;
import net.minecraftforge.common.MinecraftForge;

public class BloodExtractorConfig extends ExtendedConfig {
    
    public static BloodExtractorConfig _instance;

    public BloodExtractorConfig() {
        super(
            4000,
            "Blood Extractor",
            "bloodextractor",
            null,
            BloodExtractor.class
        );
    }
    
    @Override
    public void onRegistered() {
        MinecraftForge.EVENT_BUS.register(new BloodObtainLivingDeathEventHook());
    }
    
}
