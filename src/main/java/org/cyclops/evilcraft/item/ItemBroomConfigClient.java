package org.cyclops.evilcraft.item;

import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.client.render.model.ItemModelBroom;

/**
 * @author rubensworks
 */
public class ItemBroomConfigClient extends ItemClientConfig<ModBaseNeoForge<?>> {
    public ItemBroomConfigClient(ItemConfigCommon<ModBaseNeoForge<?>> itemConfig) {
        super(itemConfig);
        itemConfig.getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(ItemModelBroom.Unbaked.ID, ItemModelBroom.Unbaked.MAP_CODEC));
    }
}
