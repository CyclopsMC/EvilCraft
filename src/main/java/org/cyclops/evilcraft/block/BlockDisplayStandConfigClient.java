package org.cyclops.evilcraft.block;

import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.client.render.model.ItemModelDisplayStand;
import org.cyclops.evilcraft.client.render.model.ModelDisplayStandBaked;

/**
 * Config for the {@link BlockDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockDisplayStandConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockDisplayStandConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(ItemModelDisplayStand.Unbaked.ID, ItemModelDisplayStand.Unbaked.MAP_CODEC));
        blockConfig.getMod().getModEventBus().addListener((RegisterBlockStateModels event) -> event.registerModel(ModelDisplayStandBaked.Unbaked.ID, ModelDisplayStandBaked.Unbaked.CODEC));
    }
}
