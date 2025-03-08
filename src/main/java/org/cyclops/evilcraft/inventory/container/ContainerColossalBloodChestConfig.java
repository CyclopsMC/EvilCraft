package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerColossalBloodChest}.
 * @author rubensworks
 */
public class ContainerColossalBloodChestConfig extends GuiConfigCommon<ContainerColossalBloodChest, IModBase> {

    public ContainerColossalBloodChestConfig() {
        super(EvilCraft._instance,
                "colossal_blood_chest",
                eConfig -> new MenuType<>(ContainerColossalBloodChest::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerColossalBloodChest> getScreenFactoryProvider() {
        return new ContainerColossalBloodChestConfigScreenFactoryProvider();
    }
}
