package evilcraft.entity.monster;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.client.render.entity.RenderVengeanceSpirit;
import evilcraft.core.IMCHandler;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.MobConfig;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.entity.monster.VengeanceSpirit.SpiritBlacklistChanged;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import org.apache.logging.log4j.Level;

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

    @Override
    public void onInit(IInitListener.Step step) {
        super.onInit(step);
        if(step == IInitListener.Step.INIT) {
            EvilCraft.IMC_HANDLER.registerAction(Reference.IMC_BLACKLIST_VENGEANCESPIRIT, new IMCHandler.IIMCAction() {

                @Override
                public boolean handle(FMLInterModComms.IMCMessage message) {
                    if(!message.isStringMessage()) return false;
                    try {
                        Class<EntityLivingBase> clazz = (Class<EntityLivingBase>) Class.forName(message.getStringValue());
                        VengeanceSpirit.addToBlacklistIMC(clazz);
                    } catch (ClassNotFoundException e) {
                        EvilCraft.log("IMC blacklist vengeance spirit did not provide an existing class.", Level.ERROR);
                        return false;
                    } catch (ClassCastException e) {
                        EvilCraft.log("IMC blacklist vengeance spirit did not provide a class of type EntityLivingBase.",
                                Level.ERROR);
                        return false;
                    }
                    return true;
                }
            });
        }
    }
    
}
