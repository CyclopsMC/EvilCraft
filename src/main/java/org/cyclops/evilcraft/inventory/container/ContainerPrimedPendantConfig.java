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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenPrimedPendant;

/**
 * Config for {@link ContainerPrimedPendant}.
 * @author rubensworks
 */
public class ContainerPrimedPendantConfig extends GuiConfig<ContainerPrimedPendant> {

    public ContainerPrimedPendantConfig() {
        super(EvilCraft._instance,
                "primed_pendant",
                eConfig -> new ContainerTypeData<>(ContainerPrimedPendant::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerPrimedPendant>> ScreenManager.IScreenFactory<ContainerPrimedPendant, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenPrimedPendant::new);
    }

}
