package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.tileentity.tickaction.purifier.DisenchantPurifyAction;

/**
 * Config for the Blook.
 * @author rubensworks
 *
 */
public class BlookConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BlookConfig _instance;

    /**
     * Make a new instance.
     */
    public BlookConfig() {
        super(
                EvilCraft._instance,
            true,
            "blook",
            null,
            null
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        DisenchantPurifyAction.ALLOWED_BOOK.set(getItemInstance());
    }
}
