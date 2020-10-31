package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileEternalWater}.
 * @author rubensworks
 *
 */
public class TileEternalWaterConfig extends TileEntityConfig<TileEternalWater> {

    public TileEternalWaterConfig() {
        super(
                EvilCraft._instance,
                "eternal_water_block",
                (eConfig) -> new TileEntityType<>(TileEternalWater::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ETERNAL_WATER), null)
        );
    }

}
