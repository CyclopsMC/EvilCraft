package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenBloodChest;

/**
 * @author rubensworks
 */
public class ContainerBloodChestConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerBloodChest> {
    @Override
    public <U extends Screen & MenuAccess<ContainerBloodChest>> MenuScreens.ScreenConstructor<ContainerBloodChest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenBloodChest::new);
    }
}
