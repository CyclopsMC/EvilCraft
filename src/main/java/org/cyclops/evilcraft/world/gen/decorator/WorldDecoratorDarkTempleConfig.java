package org.cyclops.evilcraft.world.gen.decorator;

import net.minecraft.world.gen.placement.ChanceConfig;
import org.cyclops.cyclopscore.config.extendedconfig.WorldDecoratorConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link WorldDecoratorDarkTemple}.
 * @author rubensworks
 *
 */
public class WorldDecoratorDarkTempleConfig extends WorldDecoratorConfig {

    public WorldDecoratorDarkTempleConfig() {
        super(
                EvilCraft._instance,
                "dark_temple",
                eConfig -> new WorldDecoratorDarkTemple(ChanceConfig::deserialize)
        );
    }
}
