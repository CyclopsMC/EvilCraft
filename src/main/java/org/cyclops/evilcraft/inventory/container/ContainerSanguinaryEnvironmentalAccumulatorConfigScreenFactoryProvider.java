package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSanguinaryEnvironmentalAccumulator;

/**
 * @author rubensworks
 */
public class ContainerSanguinaryEnvironmentalAccumulatorConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerSanguinaryEnvironmentalAccumulator> {
    @Override
    public <U extends Screen & MenuAccess<ContainerSanguinaryEnvironmentalAccumulator>> MenuScreens.ScreenConstructor<ContainerSanguinaryEnvironmentalAccumulator, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSanguinaryEnvironmentalAccumulator::new);
    }
}
