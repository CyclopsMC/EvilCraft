package evilcraft.entity.monster;

import evilcraft.EvilCraft;
import evilcraft.client.render.entity.RenderVengeanceSpirit;
import evilcraft.entity.monster.VengeanceSpirit.SpiritBlacklistChanged;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;

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
                EvilCraft._instance,
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
        return Helpers.RGBToInt(64, 16, 93);
    }

    @Override
    public int getForegroundEggColor() {
        return Helpers.RGBToInt(134, 60, 169);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager) {
        return new RenderVengeanceSpirit(renderManager, this);
    }
    
}
