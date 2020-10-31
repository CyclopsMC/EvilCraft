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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenBloodChest;

/**
 * Config for {@link ContainerBloodChest}.
 * @author rubensworks
 */
public class ContainerBloodChestConfig extends GuiConfig<ContainerBloodChest> {

    public ContainerBloodChestConfig() {
        super(EvilCraft._instance,
                "blood_chest",
                eConfig -> new ContainerType<>(ContainerBloodChest::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerBloodChest>> ScreenManager.IScreenFactory<ContainerBloodChest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenBloodChest::new);
    }

}
