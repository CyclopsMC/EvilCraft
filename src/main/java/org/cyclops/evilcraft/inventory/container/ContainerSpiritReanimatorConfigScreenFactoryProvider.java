package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSpiritReanimator;

/**
 * @author rubensworks
 */
public class ContainerSpiritReanimatorConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerSpiritReanimator> {
    @Override
    public <U extends Screen & MenuAccess<ContainerSpiritReanimator>> MenuScreens.ScreenConstructor<ContainerSpiritReanimator, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSpiritReanimator::new);
    }
}
