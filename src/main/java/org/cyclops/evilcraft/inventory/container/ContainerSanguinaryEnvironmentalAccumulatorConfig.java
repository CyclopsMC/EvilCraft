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
import org.cyclops.evilcraft.client.gui.container.ContainerScreenSanguinaryEnvironmentalAccumulator;

/**
 * Config for {@link ContainerSanguinaryEnvironmentalAccumulator}.
 * @author rubensworks
 */
public class ContainerSanguinaryEnvironmentalAccumulatorConfig extends GuiConfig<ContainerSanguinaryEnvironmentalAccumulator> {

    public ContainerSanguinaryEnvironmentalAccumulatorConfig() {
        super(EvilCraft._instance,
                "sanguinary_environmental_accumulator",
                eConfig -> new MenuType<>(ContainerSanguinaryEnvironmentalAccumulator::new, FeatureFlags.VANILLA_SET));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & MenuAccess<ContainerSanguinaryEnvironmentalAccumulator>> MenuScreens.ScreenConstructor<ContainerSanguinaryEnvironmentalAccumulator, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenSanguinaryEnvironmentalAccumulator::new);
    }

}
