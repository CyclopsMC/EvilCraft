package org.cyclops.evilcraft.block;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

import java.util.List;

/**
 * Config for the {@link BlockBloodStain}.
 * @author rubensworks
 *
 */
public class BlockBloodStainConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "block", comment = "The amount of blood per HP (2HP = 1 heart) of the max mob health that will be added to this blockState when a mob dies from fall damage.", isCommandable = true)
    public static int bloodMBPerHP = 20;

    @ConfigurablePropertyCommon(category = "block", comment = "Blocks onto which no blood stains can be spawned. Regular expressions are allowed.")
    public static List<String> spawnBlacklist = Lists.newArrayList(
            "tconstruct:.*"
    );
    @ConfigurablePropertyCommon(category = "block", comment = "If blood stains should be spawned on block entities.")
    public static boolean spawnOnBlockEntities = false;

    public BlockBloodStainConfig() {
        super(
                EvilCraft._instance,
                "blood_stain",
                (eConfig, properties) -> new BlockBloodStain(properties
                        .noCollission()
                        .strength(0.5F)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
