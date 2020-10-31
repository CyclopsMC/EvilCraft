package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSanguinaryEnvironmentalAccumulator;

/**
 * Config for {@link ContainerSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 */
public class ContainerSanguinaryEnvironmentalAccumulatorConfig extends GuiConfig<ContainerSanguinaryEnvironmentalAccumulator> {

    public ContainerSanguinaryEnvironmentalAccumulatorConfig() {
        super(EvilCraft._instance,
                "sanguinary_environmental_accumulator",
                eConfig -> new ContainerType<>(ContainerSanguinaryEnvironmentalAccumulator::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerSanguinaryEnvironmentalAccumulator>> ScreenManager.IScreenFactory<ContainerSanguinaryEnvironmentalAccumulator, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSanguinaryEnvironmentalAccumulator::new);
    }

}
