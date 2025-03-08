package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenPrimedPendant;

/**
 * @author rubensworks
 */
public class ContainerPrimedPendantConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerPrimedPendant> {
    @Override
    public <U extends Screen & MenuAccess<ContainerPrimedPendant>> MenuScreens.ScreenConstructor<ContainerPrimedPendant, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenPrimedPendant::new);
    }
}
