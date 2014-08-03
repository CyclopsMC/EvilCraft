package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.MobConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.render.entity.RenderNetherfish;

/**
 * Config for the {@link Netherfish}.
 * @author rubensworks
 *
 */
public class NetherfishConfig extends MobConfig {
    
    /**
     * The unique instance.
     */
    public static NetherfishConfig _instance;
    
    /**
     * Should the Netherfish be enabled?
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Netherfish be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;    

    /**
     * Make a new instance.
     */
    public NetherfishConfig() {
        super(
        	true,
            "netherfish",
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
        return Helpers.RGBToInt(73, 27, 20);
    }

    @Override
    public int getForegroundEggColor() {
        return Helpers.RGBToInt(160, 45, 27);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderNetherfish(this);
    }
    
}
