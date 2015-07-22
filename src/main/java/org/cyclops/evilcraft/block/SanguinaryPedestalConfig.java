package org.cyclops.evilcraft.block;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link SanguinaryPedestal}.
 * @author rubensworks
 *
 */
public class SanguinaryPedestalConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static SanguinaryPedestalConfig _instance;

    /**
     * Blood multiplier when Efficiency is active.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "Blood multiplier when Efficiency is active.", isCommandable = true)
    public static double efficiencyBoost = 1.5D;

    /**
     * Make a new instance.
     */
    public SanguinaryPedestalConfig() {
        super(
                EvilCraft._instance,
        	true,
            "sanguinaryPedestal",
            null,
            SanguinaryPedestal.class
        );
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if(itemStack.getItemDamage() == 0) {
            return super.getModelName(itemStack) + "_tier0";
        } else {
            return super.getModelName(itemStack) + "_tier1";
        }
    }
    
}
