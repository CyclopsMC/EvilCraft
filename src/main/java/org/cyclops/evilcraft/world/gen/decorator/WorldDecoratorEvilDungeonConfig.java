package org.cyclops.evilcraft.world.gen.decorator;

import net.minecraft.world.gen.placement.ChanceConfig;
import org.cyclops.cyclopscore.config.extendedconfig.WorldDecoratorConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link WorldDecoratorEvilDungeon}.
 * @author rubensworks
 *
 */
public class WorldDecoratorEvilDungeonConfig extends WorldDecoratorConfig {

    public WorldDecoratorEvilDungeonConfig() {
        super(
                EvilCraft._instance,
                "evil_dungeon",
                eConfig -> new WorldDecoratorEvilDungeon(ChanceConfig::deserialize)
        );
    }
}
