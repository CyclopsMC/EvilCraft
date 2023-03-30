package org.cyclops.evilcraft.inventory.container;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.gui.container.ContainerScreenPrimedPendant;

/**
 * Config for {@link ContainerPrimedPendant}.
 * @author rubensworks
 */
public class ContainerPrimedPendantConfig extends GuiConfig<ContainerPrimedPendant> {

    public ContainerPrimedPendantConfig() {
        super(EvilCraft._instance,
                "primed_pendant",
                eConfig -> new ContainerTypeData<>(ContainerPrimedPendant::new, FeatureFlags.VANILLA_SET));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & MenuAccess<ContainerPrimedPendant>> MenuScreens.ScreenConstructor<ContainerPrimedPendant, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenPrimedPendant::new);
    }

}
