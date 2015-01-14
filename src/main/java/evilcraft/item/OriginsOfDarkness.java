package evilcraft.item;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemGui;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

/**
 * A simple orb that can be filled with blood.
 * @author rubensworks
 *
 */
public class OriginsOfDarkness extends ItemGui {

    private static OriginsOfDarkness _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new OriginsOfDarkness(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static OriginsOfDarkness getInstance() {
        return _instance;
    }

    private OriginsOfDarkness(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);

        if (MinecraftHelpers.isClientSide())
            setGUI(GuiOriginsOfDarkness.class);

        // We don't set a container, since this book does not require any server component.
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.uncommon;
    }

    @Override
    protected boolean isClientSideOnlyGui() {
        return true;
    }

}
