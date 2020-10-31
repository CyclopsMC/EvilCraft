package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSpiritReanimator;

/**
 * Config for {@link ContainerSpiritReanimator}.
 * @author rubensworks
 */
public class ContainerSpiritReanimatorConfig extends GuiConfig<ContainerSpiritReanimator> {

    public ContainerSpiritReanimatorConfig() {
        super(EvilCraft._instance,
                "spirit_reanimator",
                eConfig -> new ContainerType<>(ContainerSpiritReanimator::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerSpiritReanimator>> ScreenManager.IScreenFactory<ContainerSpiritReanimator, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSpiritReanimator::new);
    }

}
