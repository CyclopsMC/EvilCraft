package org.cyclops.evilcraft.item;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.client.gui.container.GuiOriginsOfDarkness;

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
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public Class<? extends Container> getContainer() {
        // We don't set a container, since this book does not require any server component.
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiOriginsOfDarkness.class;
    }

    @Override
    protected boolean isClientSideOnlyGui() {
        return true;
    }

}
