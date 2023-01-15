package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.blockentity.tickaction.purifier.DisenchantPurifyAction;

/**
 * Config for the Blook.
 * @author rubensworks
 *
 */
public class ItemBlookConfig extends ItemConfig {

    public ItemBlookConfig() {
        super(
                EvilCraft._instance,
            "blook",
                eConfig -> new Item(new Item.Properties()
                        )
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        DisenchantPurifyAction.ALLOWED_BOOK.set(getInstance());
    }
}
