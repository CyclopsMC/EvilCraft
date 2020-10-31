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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSpiritFurnace;

/**
 * Config for {@link ContainerSpiritFurnace}.
 * @author rubensworks
 */
public class ContainerSpiritFurnaceConfig extends GuiConfig<ContainerSpiritFurnace> {

    public ContainerSpiritFurnaceConfig() {
        super(EvilCraft._instance,
                "spirit_furnace",
                eConfig -> new ContainerType<>(ContainerSpiritFurnace::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerSpiritFurnace>> ScreenManager.IScreenFactory<ContainerSpiritFurnace, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSpiritFurnace::new);
    }

}
