package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenColossalBloodChest;

/**
 * @author rubensworks
 */
public class ContainerColossalBloodChestConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerColossalBloodChest> {
    @Override
    public <U extends Screen & MenuAccess<ContainerColossalBloodChest>> MenuScreens.ScreenConstructor<ContainerColossalBloodChest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenColossalBloodChest::new);
    }
}
