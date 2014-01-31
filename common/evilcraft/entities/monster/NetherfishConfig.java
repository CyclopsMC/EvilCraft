package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.EntityLiving;
import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.MobConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.render.ModelRender;

public class NetherfishConfig extends MobConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Netherfish be enabled?")
    public static boolean isEnabled = true;
    
    public static NetherfishConfig _instance;

    public NetherfishConfig() {
        super(
            Reference.MOB_NETHERFISH,
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
