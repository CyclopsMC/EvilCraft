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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenBloodInfuser;

/**
 * Config for {@link ContainerBloodInfuser}.
 * @author rubensworks
 */
public class ContainerBloodInfuserConfig extends GuiConfig<ContainerBloodInfuser> {

    public ContainerBloodInfuserConfig() {
        super(EvilCraft._instance,
                "blood_infuser",
                eConfig -> new ContainerType<>(ContainerBloodInfuser::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerBloodInfuser>> ScreenManager.IScreenFactory<ContainerBloodInfuser, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenBloodInfuser::new);
    }

}
