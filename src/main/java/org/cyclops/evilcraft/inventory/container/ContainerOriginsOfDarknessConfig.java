package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenOriginsOfDarkness;

/**
 * Config for {@link ContainerOriginsOfDarkness}.
 * @author rubensworks
 */
public class ContainerOriginsOfDarknessConfig extends GuiConfig<ContainerOriginsOfDarkness> {

    public ContainerOriginsOfDarknessConfig() {
        super(EvilCraft._instance,
                "origins_of_darkness",
                eConfig -> new ContainerTypeData<>(ContainerOriginsOfDarkness::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerOriginsOfDarkness>> ScreenManager.IScreenFactory<ContainerOriginsOfDarkness, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenOriginsOfDarkness::new);
    }

}
