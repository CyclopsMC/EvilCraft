package org.cyclops.evilcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

import java.util.List;

/**
 * A simple orb that can be filled with blood.
 * @author rubensworks
 *
 */
public class BloodOrb extends ConfigurableItem {

    private static BloodOrb _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodOrb getInstance() {
        return _instance;
    }

    public BloodOrb(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < 2; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String suffix = "empty";
        if(itemStack.getItemDamage() == 1) suffix = "filled";
        return super.getUnlocalizedName(itemStack) + "." + suffix;
    }

}
