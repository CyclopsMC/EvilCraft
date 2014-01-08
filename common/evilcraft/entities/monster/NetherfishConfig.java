package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.MobConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.proxies.ClientProxy;

public class NetherfishConfig extends MobConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Netherfish be enabled?")
    public static boolean isEnabled = true;
    
    public static NetherfishConfig _instance;

    public NetherfishConfig() {
        super(
            1,
            "Netherfish",
            "netherFish",
            null,
            Netherfish.class
        );
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public int getBackgroundEggColor() {
        return 123456;
    }

    @Override
    public int getForegroundEggColor() {
        return 654321;
    }

    @Override
    public Render getRender() {
        return new RenderSilverfish();
    }
    
}
