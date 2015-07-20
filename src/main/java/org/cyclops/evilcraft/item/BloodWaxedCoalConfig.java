package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Blood-Waxed Coal.
 * @author rubensworks
 *
 */
public class BloodWaxedCoalConfig extends ItemConfig implements IFuelHandler {

    /**
     * The unique instance.
     */
    public static BloodWaxedCoalConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWaxedCoalConfig() {
        super(
                EvilCraft._instance,
        	true,
            "bloodWaxedCoal",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this);
    }
    
    @Override
    public void onRegistered() {
    	GameRegistry.registerFuelHandler(this);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if(getItemInstance() == fuel.getItem()) {
            return 3200;
        }
        return 0;
    }

}
