package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenBloodInfuser;

/**
 * @author rubensworks
 */
public class ContainerBloodInfuserConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerBloodInfuser> {
    @Override
    public <U extends Screen & MenuAccess<ContainerBloodInfuser>> MenuScreens.ScreenConstructor<ContainerBloodInfuser, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenBloodInfuser::new);
    }
}
