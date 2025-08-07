package org.cyclops.evilcraft.item;

import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ItemClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.client.render.model.ItemModelBroomPart;

/**
 * @author rubensworks
 */
public class ItemBroomPartConfigClient extends ItemClientConfig<ModBaseNeoForge<?>> {
    public ItemBroomPartConfigClient(ItemConfigCommon<ModBaseNeoForge<?>> itemConfig) {
        super(itemConfig);
        itemConfig.getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(ItemModelBroomPart.Unbaked.ID, ItemModelBroomPart.Unbaked.MAP_CODEC));
    }
}
