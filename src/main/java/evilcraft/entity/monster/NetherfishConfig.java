package evilcraft.entity.monster;

import evilcraft.client.render.entity.RenderNetherfish;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Should the Netherfish be enabled?", requiresMcRestart = true)
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
        return RenderHelpers.RGBToInt(73, 27, 20);
    }

    @Override
    public int getForegroundEggColor() {
        return RenderHelpers.RGBToInt(160, 45, 27);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager) {
        return new RenderNetherfish(renderManager, this);
    }
    
}
