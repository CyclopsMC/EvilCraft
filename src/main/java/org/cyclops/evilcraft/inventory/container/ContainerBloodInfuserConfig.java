package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerBloodInfuser}.
 * @author rubensworks
 */
public class ContainerBloodInfuserConfig extends GuiConfigCommon<ContainerBloodInfuser, IModBase> {

    public ContainerBloodInfuserConfig() {
        super(EvilCraft._instance,
                "blood_infuser",
                eConfig -> new MenuType<>(ContainerBloodInfuser::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerBloodInfuser> getScreenFactoryProvider() {
        return new ContainerBloodInfuserConfigScreenFactoryProvider();
    }
}
