package evilcraft.entity.monster;

import evilcraft.EvilCraft;
import evilcraft.client.render.entity.RenderWerewolf;
import evilcraft.client.render.model.ModelWerewolf;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;

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
        return RenderHelpers.RGBToInt(105, 67, 18);
    }

    @Override
    public int getForegroundEggColor() {
        return RenderHelpers.RGBToInt(57, 25, 10);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager) {
        return new RenderWerewolf(renderManager, this, new ModelWerewolf(), 0.5F);
    }
    
}
