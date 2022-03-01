package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntitySanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class BlockEntitySanguinaryEnvironmentalAccumulatorConfig extends BlockEntityConfig<BlockEntitySanguinaryEnvironmentalAccumulator> {

    public BlockEntitySanguinaryEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "sanguinary_environmental_accumulator",
                (eConfig) -> new BlockEntityType<>(BlockEntitySanguinaryEnvironmentalAccumulator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR), null)
        );
    }

}
