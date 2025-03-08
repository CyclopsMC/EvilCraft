package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenExaltedCrafter;

/**
 * @author rubensworks
 */
public class ContainerExaltedCrafterConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerExaltedCrafter> {
    @Override
    public <U extends Screen & MenuAccess<ContainerExaltedCrafter>> MenuScreens.ScreenConstructor<ContainerExaltedCrafter, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenExaltedCrafter::new);
    }
}
