package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 */
public class ContainerSanguinaryEnvironmentalAccumulatorConfig extends GuiConfigCommon<ContainerSanguinaryEnvironmentalAccumulator, IModBase> {

    public ContainerSanguinaryEnvironmentalAccumulatorConfig() {
        super(EvilCraft._instance,
                "sanguinary_environmental_accumulator",
                eConfig -> new MenuType<>(ContainerSanguinaryEnvironmentalAccumulator::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerSanguinaryEnvironmentalAccumulator> getScreenFactoryProvider() {
        return new ContainerSanguinaryEnvironmentalAccumulatorConfigScreenFactoryProvider();
    }
}
