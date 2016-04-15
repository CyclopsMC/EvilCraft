package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.entity.RenderNetherfish;

/**
 * Config for the {@link Netherfish}.
 * @author rubensworks
 *
 */
public class NetherfishConfig extends MobConfig<Netherfish> {
    
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
                EvilCraft._instance,
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
    public Render getRender(RenderManager renderManager) {
        return new RenderNetherfish(renderManager, this);
    }
    
}
