package evilcraft.item;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
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
     * Get the unique instance.
     * @return The instance.
     */
    public static OriginsOfDarkness getInstance() {
        return _instance;
    }

    public OriginsOfDarkness(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);

        if (MinecraftHelpers.isClientSide())
            setGUI(GuiOriginsOfDarkness.class);

        // We don't set a container, since this book does not require any server component.
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    protected boolean isClientSideOnlyGui() {
        return true;
    }

}
