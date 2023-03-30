package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
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
                eConfig -> new MenuType<>(ContainerSpiritReanimator::new, FeatureFlags.VANILLA_SET));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & MenuAccess<ContainerSpiritReanimator>> MenuScreens.ScreenConstructor<ContainerSpiritReanimator, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSpiritReanimator::new);
    }

}
