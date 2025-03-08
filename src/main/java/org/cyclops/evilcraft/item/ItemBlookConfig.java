package org.cyclops.evilcraft.item;

import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.blockentity.tickaction.purifier.DisenchantPurifyAction;

/**
 * Config for the Blook.
 * @author rubensworks
 *
 */
public class ItemBlookConfig extends ItemConfigCommon<IModBase> {

    public ItemBlookConfig() {
        super(
                EvilCraft._instance,
            "blook",
                (eConfig, properties) -> new Item(properties)
        );
    }

    @Override
    public void onRegistryRegistered() {
        super.onRegistryRegistered();
        DisenchantPurifyAction.ALLOWED_BOOK.set(getInstance());
    }
}
