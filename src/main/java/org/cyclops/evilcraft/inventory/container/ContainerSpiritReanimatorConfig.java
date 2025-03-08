package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerSpiritReanimator}.
 * @author rubensworks
 */
public class ContainerSpiritReanimatorConfig extends GuiConfigCommon<ContainerSpiritReanimator, IModBase> {

    public ContainerSpiritReanimatorConfig() {
        super(EvilCraft._instance,
                "spirit_reanimator",
                eConfig -> new MenuType<>(ContainerSpiritReanimator::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerSpiritReanimator> getScreenFactoryProvider() {
        return new ContainerSpiritReanimatorConfigScreenFactoryProvider();
    }
}
