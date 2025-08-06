package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link BlockEntityEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class BlockEntityEnvironmentalAccumulatorConfig extends BlockEntityConfigCommon<BlockEntityEnvironmentalAccumulator, IModBase> {

    public BlockEntityEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                (eConfig) -> new BlockEntityType<>(BlockEntityEnvironmentalAccumulator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR.get()))
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityEnvironmentalAccumulator.CapabilityRegistrar(this::getInstance)::register);
    }

}
