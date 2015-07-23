package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood Orb.
 * @author rubensworks
 *
 */
public class BloodOrbConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodOrbConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodOrbConfig() {
        super(
                EvilCraft._instance,
        	true,
            "bloodOrb",
            null,
            BloodOrb.class
        );
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        if (itemStack.getMetadata() == 1) {
            return super.getModelName(itemStack) + "_filled";
        }
        return super.getModelName(itemStack);
    }
    
}
