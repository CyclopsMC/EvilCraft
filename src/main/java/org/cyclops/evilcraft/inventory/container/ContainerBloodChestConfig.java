package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerBloodChest}.
 * @author rubensworks
 */
public class ContainerBloodChestConfig extends GuiConfigCommon<ContainerBloodChest, IModBase> {

    public ContainerBloodChestConfig() {
        super(EvilCraft._instance,
                "blood_chest",
                eConfig -> new MenuType<>(ContainerBloodChest::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerBloodChest> getScreenFactoryProvider() {
        return new ContainerBloodChestConfigScreenFactoryProvider();
    }
}
