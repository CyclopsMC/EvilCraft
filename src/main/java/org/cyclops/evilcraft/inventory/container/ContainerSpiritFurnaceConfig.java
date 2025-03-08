package org.cyclops.evilcraft.inventory.container;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ContainerSpiritFurnace}.
 * @author rubensworks
 */
public class ContainerSpiritFurnaceConfig extends GuiConfigCommon<ContainerSpiritFurnace, IModBase> {

    public ContainerSpiritFurnaceConfig() {
        super(EvilCraft._instance,
                "spirit_furnace",
                eConfig -> new MenuType<>(ContainerSpiritFurnace::new, FeatureFlags.VANILLA_SET));
    }

    @Override
    public GuiConfigScreenFactoryProvider<ContainerSpiritFurnace> getScreenFactoryProvider() {
        return new ContainerSpiritFurnaceConfigScreenFactoryProvider();
    }
}
