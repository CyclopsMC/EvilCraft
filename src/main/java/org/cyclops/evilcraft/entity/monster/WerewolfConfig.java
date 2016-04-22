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
import org.cyclops.evilcraft.client.render.entity.RenderWerewolf;
import org.cyclops.evilcraft.client.render.model.ModelWerewolf;

/**
 * Config for the {@link Werewolf}.
 * @author rubensworks
 *
 */
public class WerewolfConfig extends MobConfig<Werewolf> {
    
    /**
     * The unique instance.
     */
    public static WerewolfConfig _instance;
    
    /**
     * Should the Werewolf be enabled?
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Should the Werewolf be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;    

    /**
     * Make a new instance.
     */
    public WerewolfConfig() {
        super(
                EvilCraft._instance,
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
    public Render<Werewolf> getRender(RenderManager renderManager) {
        return new RenderWerewolf(renderManager, this, new ModelWerewolf(), 0.5F);
    }
    
}
