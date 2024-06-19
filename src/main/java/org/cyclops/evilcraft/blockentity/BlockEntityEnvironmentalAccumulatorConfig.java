package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderBlockEntityEnvironmentalAccumulator;

/**
 * Config for the {@link BlockEntityEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class BlockEntityEnvironmentalAccumulatorConfig extends BlockEntityConfig<BlockEntityEnvironmentalAccumulator> {

    public BlockEntityEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                (eConfig) -> new BlockEntityType<>(BlockEntityEnvironmentalAccumulator::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR.get()), null)
        );
        EvilCraft._instance.getModEventBus().addListener(new BlockEntityEnvironmentalAccumulator.CapabilityRegistrar(this::getInstance)::register);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderBlockEntityEnvironmentalAccumulator::new);
    }

}
