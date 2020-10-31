package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class TileSanguinaryEnvironmentalAccumulatorConfig extends TileEntityConfig<TileSanguinaryEnvironmentalAccumulator> {

    public TileSanguinaryEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "sanguinary_environmental_accumulator",
                (eConfig) -> new TileEntityType<>(TileSanguinaryEnvironmentalAccumulator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR), null)
        );
    }

}
