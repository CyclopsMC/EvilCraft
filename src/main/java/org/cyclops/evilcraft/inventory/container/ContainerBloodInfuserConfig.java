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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenBloodInfuser;

/**
 * Config for {@link ContainerBloodInfuser}.
 * @author rubensworks
 */
public class ContainerBloodInfuserConfig extends GuiConfig<ContainerBloodInfuser> {

    public ContainerBloodInfuserConfig() {
        super(EvilCraft._instance,
                "blood_infuser",
                eConfig -> new MenuType<>(ContainerBloodInfuser::new, FeatureFlags.VANILLA_SET));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & MenuAccess<ContainerBloodInfuser>> MenuScreens.ScreenConstructor<ContainerBloodInfuser, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenBloodInfuser::new);
    }

}
