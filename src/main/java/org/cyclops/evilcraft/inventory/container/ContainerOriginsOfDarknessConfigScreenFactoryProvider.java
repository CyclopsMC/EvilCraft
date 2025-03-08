package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenOriginsOfDarkness;

/**
 * @author rubensworks
 */
public class ContainerOriginsOfDarknessConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerOriginsOfDarkness> {
    @Override
    public <U extends Screen & MenuAccess<ContainerOriginsOfDarkness>> MenuScreens.ScreenConstructor<ContainerOriginsOfDarkness, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenOriginsOfDarkness::new);
    }
}
