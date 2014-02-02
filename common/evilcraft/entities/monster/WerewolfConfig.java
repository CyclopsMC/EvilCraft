package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.MobConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.render.entity.RenderWerewolf;
import evilcraft.render.models.WerewolfModel;

public class WerewolfConfig extends MobConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Werewolf be enabled?")
    public static boolean isEnabled = true;
    
    public static WerewolfConfig _instance;

    public WerewolfConfig() {
        super(
            Reference.MOB_WEREWOLF,
            "Werewolf",
            "werewolf",
            null,
            Werewolf.class
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

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderWerewolf(this, new WerewolfModel(), 0.5F);
    }
    
}
