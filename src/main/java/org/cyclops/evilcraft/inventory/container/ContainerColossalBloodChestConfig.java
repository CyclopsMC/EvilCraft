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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenColossalBloodChest;

/**
 * Config for {@link ContainerColossalBloodChest}.
 * @author rubensworks
 */
public class ContainerColossalBloodChestConfig extends GuiConfig<ContainerColossalBloodChest> {

    public ContainerColossalBloodChestConfig() {
        super(EvilCraft._instance,
                "colossal_blood_chest",
                eConfig -> new ContainerType<>(ContainerColossalBloodChest::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerColossalBloodChest>> ScreenManager.IScreenFactory<ContainerColossalBloodChest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenColossalBloodChest::new);
    }

}
