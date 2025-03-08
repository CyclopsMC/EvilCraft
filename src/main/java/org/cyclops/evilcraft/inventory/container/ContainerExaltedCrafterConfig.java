package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerTypeData;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerExaltedCrafter}.
 * @author rubensworks
 */
public class ContainerExaltedCrafterConfig extends GuiConfigCommon<ContainerExaltedCrafter, IModBase> {

    public ContainerExaltedCrafterConfig() {
        super(EvilCraft._instance,
                "exalted_crafter",
                eConfig -> new ContainerTypeData<>(ContainerExaltedCrafter::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerExaltedCrafter> getScreenFactoryProvider() {
        return new ContainerExaltedCrafterConfigScreenFactoryProvider();
    }
}
