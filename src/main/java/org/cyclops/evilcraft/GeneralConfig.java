package org.cyclops.evilcraft;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;


/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "general", comment = "Evil stuff...", isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static boolean dieWithoutAnyReason = false;

    @ConfigurablePropertyCommon(category = "general", comment = "If farting is enabled on this server; Client-side: If farting can be seen at your client.", isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static boolean farting = true;

    /*@ConfigurablePropertyCommon(category = "fluid",
            comment = "The allowed blood conversions with their ratio. (ratio 2 means that this "
                    + "fluid is 1mB of this fluid can be converted into 2mB of EvilCraft Blood.",
            configLocation = ModConfigLocation.SERVER)
    public static List<String> bloodConverters = Lists.newArrayList(
            "blood:1.0",
            "lifeessence:1.0",
            "hell_blood:1.0"
    );*/

    @ConfigurablePropertyCommon(category = "general", comment = "The amount of mB that can flow per tick out of machines and items.", requiresMcRestart = true, configLocation = ModConfigLocation.SERVER)
    public static int mbFlowRate = 100;

    @ConfigurablePropertyCommon(category = "worldgeneration", comment = "Spawn extra silverfish blocks in all biomes.", configLocation = ModConfigLocation.SERVER)
    public static boolean extraSilverfish = false;

    @ConfigurablePropertyCommon(category = "general", comment = "If the blood gui overlay should be rendered.", isCommandable = true, configLocation = ModConfigLocation.CLIENT)
    public static boolean bloodGuiOverlay = true;
    @ConfigurablePropertyCommon(category = "general", comment = "The position to render the blood gui overlay at. (0=NE, 1=SE, 2=SW,3=NW)", isCommandable = true, configLocation = ModConfigLocation.CLIENT)
    public static int bloodGuiOverlayPosition = 1;
    @ConfigurablePropertyCommon(category = "general", comment = "The X offset for the blood gui overlay.", isCommandable = true, configLocation = ModConfigLocation.CLIENT)
    public static int bloodGuiOverlayPositionOffsetX = -5;
    @ConfigurablePropertyCommon(category = "general", comment = "The Y offset for the blood gui overlay.", isCommandable = true, configLocation = ModConfigLocation.CLIENT)
    public static int bloodGuiOverlayPositionOffsetY = -5;

    @ConfigurablePropertyCommon(category = "worldgeneration", comment = "The spawn chance for loot chests in dark temples, set to zero to completely disable.", isCommandable = true, requiresMcRestart = true, configLocation = ModConfigLocation.SERVER)
    public static double darkTempleChestChance = 0.15D;

    @ConfigurablePropertyCommon(category = "general", comment = "If the broom smash modifier should be enabled to allow breaking blocks.", isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static boolean broomSmashEnabled = true;

    public GeneralConfig() {
        super(EvilCraft._instance, "general");
    }
}
