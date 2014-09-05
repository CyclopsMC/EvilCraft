package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.Render;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.MobConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;
import evilcraft.core.helpers.RenderHelpers;
import evilcraft.entities.monster.VengeanceSpirit.SpiritBlacklistChanged;
import evilcraft.render.entity.RenderVengeanceSpirit;

/**
 * Config for the {@link Netherfish}.
 * @author rubensworks
 *
 */
public class VengeanceSpiritConfig extends MobConfig {
    
    /**
     * The unique instance.
     */
    public static VengeanceSpiritConfig _instance;
    
    /**
     * Should the Vengeance Spirit be enabled?
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "Should the Vengeance Spirit be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;
    
    /**
     * The maximum amount of vengeance spirits naturally spawnable in the spawnLimitArea.
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "The maximum amount of vengeance spirits naturally spawnable in the spawnLimitArea.")
    public static int spawnLimit = 5;
    
    /**
     * The area in which the spawn limit will be checked on each spawn attempt.
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB, comment = "The area in which the spawn limit will be checked on each spawn attempt.")
    public static int spawnLimitArea = 5;
    
    /**
     * The blacklisted entity spirits, by entity name.
     */
    @ConfigurableProperty(category = ElementTypeCategory.MOB,
    		comment = "The blacklisted entity spirits, by entity name.",
    		changedCallback = SpiritBlacklistChanged.class)
    public static String[] entityBlacklist = new String[]{
    	"werewolf",
    };

    /**
     * Make a new instance.
     */
    public VengeanceSpiritConfig() {
        super(
        	true,
            "vengeanceSpirit",
            null,
            VengeanceSpirit.class
        );
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public int getBackgroundEggColor() {
        return RenderHelpers.RGBToInt(64, 16, 93);
    }

    @Override
    public int getForegroundEggColor() {
        return RenderHelpers.RGBToInt(134, 60, 169);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender() {
        return new RenderVengeanceSpirit(this);
    }
    
}
