package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerOriginsOfDarkness}.
 * @author rubensworks
 */
public class ContainerOriginsOfDarknessConfig extends GuiConfigCommon<ContainerOriginsOfDarkness, IModBase> {

    public ContainerOriginsOfDarknessConfig() {
        super(EvilCraft._instance,
                "origins_of_darkness",
                eConfig -> new ContainerTypeData<>(ContainerOriginsOfDarkness::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerOriginsOfDarkness> getScreenFactoryProvider() {
        return new ContainerOriginsOfDarknessConfigScreenFactoryProvider();
    }
}
