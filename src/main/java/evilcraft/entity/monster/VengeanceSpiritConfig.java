package evilcraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.render.entity.RenderVengeanceSpirit;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.entity.monster.VengeanceSpirit.SpiritBlacklistChanged;
import net.minecraft.client.renderer.entity.Render;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Should the Vengeance Spirit be enabled?", requiresMcRestart = true)
    public static boolean isEnabled = true;
    
    /**
     * The maximum amount of vengeance spirits naturally spawnable in the spawnLimitArea.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The maximum amount of vengeance spirits naturally spawnable in the spawnLimitArea.")
    public static int spawnLimit = 5;
    
    /**
     * The area in which the spawn limit will be checked on each spawn attempt.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The area in which the spawn limit will be checked on each spawn attempt.")
    public static int spawnLimitArea = 5;
    
    /**
     * The blacklisted entity spirits, by entity name.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB,
    		comment = "The blacklisted entity spirits, by entity name.",
    		changedCallback = SpiritBlacklistChanged.class)
    public static String[] entityBlacklist = new String[]{
    	"werewolf",
    };

    /**
     * The 1/X chance that an actual spirit will spawn when doing actions like mining with the Vengeance Pickaxe.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The 1/X chance that an actual spirit will spawn when doing actions like mining with the Vengeance Pickaxe.")
    public static int nonDegradedSpawnChance = 5;

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
