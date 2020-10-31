package org.cyclops.evilcraft.item;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.tileentity.tickaction.purifier.DisenchantPurifyAction;

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
                        .group(EvilCraft._instance.getDefaultItemGroup()))
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        DisenchantPurifyAction.ALLOWED_BOOK.set(getInstance());
    }
}
