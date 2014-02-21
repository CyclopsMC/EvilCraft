package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.MobConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.render.entity.RenderWerewolf;
import evilcraft.render.models.WerewolfModel;

/**
 * Config for the {@link Werewolf}.
 * @author rubensworks
 *
 */
public class WerewolfConfig extends MobConfig {
    
    /**
     * The unique instance.
     */
    public static WerewolfConfig _instance;
    
    /**
     * Should the Werewolf be enabled?
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Werewolf be enabled?")
    public static boolean isEnabled = true;    

    /**
     * Make a new instance.
     */
    public WerewolfConfig() {
        super(
        	true,
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
        return Helpers.RGBToInt(105, 67, 18);
    }

    @Override
    public int getForegroundEggColor() {
        return Helpers.RGBToInt(57, 25, 10);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderWerewolf(this, new WerewolfModel(), 0.5F);
    }
    
}
