package org.cyclops.evilcraft.entity.monster;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.modcompat.IMCHandler;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.entity.RenderVengeanceSpirit;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit.SpiritBlacklistChanged;

/**
 * Config for the {@link Netherfish}.
 * @author rubensworks
 *
 */
public class VengeanceSpiritConfig extends MobConfig<VengeanceSpirit> {
    
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
        "evilcraft.werewolf",
        "intangible.soul",
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
    public Render<VengeanceSpirit> getRender(RenderManager renderManager) {
        return new RenderVengeanceSpirit(renderManager, this);
    }

    @Override
    public void onInit(IInitListener.Step step) {
        super.onInit(step);
        if(step == IInitListener.Step.INIT) {
            EvilCraft._instance.getImcHandler().registerAction(Reference.IMC_BLACKLIST_VENGEANCESPIRIT, new IMCHandler.IIMCAction() {

                @Override
                public boolean handle(FMLInterModComms.IMCMessage message) {
                    if(!message.isStringMessage()) return false;
                    try {
                        Class<EntityLivingBase> clazz = (Class<EntityLivingBase>) Class.forName(message.getStringValue());
                        VengeanceSpirit.addToBlacklistIMC(clazz);
                    } catch (ClassNotFoundException e) {
                        EvilCraft.clog("IMC blacklist vengeance spirit did not provide an existing class.", Level.ERROR);
                        return false;
                    } catch (ClassCastException e) {
                        EvilCraft.clog("IMC blacklist vengeance spirit did not provide a class of type EntityLivingBase.",
                                Level.ERROR);
                        return false;
                    }
                    return true;
                }
            });
        }
    }
    
}
