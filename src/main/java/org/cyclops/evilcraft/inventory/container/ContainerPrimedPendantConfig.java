package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerPrimedPendant}.
 * @author rubensworks
 */
public class ContainerPrimedPendantConfig extends GuiConfigCommon<ContainerPrimedPendant, IModBase> {

    public ContainerPrimedPendantConfig() {
        super(EvilCraft._instance,
                "primed_pendant",
                eConfig -> new ContainerTypeData<>(ContainerPrimedPendant::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerPrimedPendant> getScreenFactoryProvider() {
        return new ContainerPrimedPendantConfigScreenFactoryProvider();
    }
}
