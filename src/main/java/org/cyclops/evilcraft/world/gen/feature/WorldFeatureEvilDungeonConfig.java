package org.cyclops.evilcraft.world.gen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.cyclops.cyclopscore.config.extendedconfig.WorldFeatureConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the evil dungeon.
 * @author rubensworks
 *
 */
public class WorldFeatureEvilDungeonConfig extends WorldFeatureConfigCommon<IModBase> {

    public WorldFeatureEvilDungeonConfig() {
        super(
                EvilCraft._instance,
                "evil_dungeon",
                eConfig -> new WorldFeatureEvilDungeon(NoneFeatureConfiguration.CODEC)
        );
    }
}
