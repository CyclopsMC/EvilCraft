package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockBloodChest}.
 * @author rubensworks
 *
 */
public class BlockEternalWaterConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "block", comment = "If the Eternal Water Block should auto-output water to adjacent blocks by default.", isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static boolean autoOutputDefault = true;

    public BlockEternalWaterConfig() {
        super(
                EvilCraft._instance,
            "eternal_water",
                (eConfig, properties) -> new BlockEternalWater(properties
                        .strength(0.5F)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
