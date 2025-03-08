package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSpiritFurnace;

/**
 * @author rubensworks
 */
public class ContainerSpiritFurnaceConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerSpiritFurnace> {
    @Override
    public <U extends Screen & MenuAccess<ContainerSpiritFurnace>> MenuScreens.ScreenConstructor<ContainerSpiritFurnace, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSpiritFurnace::new);
    }
}
