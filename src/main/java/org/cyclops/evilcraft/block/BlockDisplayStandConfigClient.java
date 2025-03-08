package org.cyclops.evilcraft.block;

import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.client.render.model.ItemModelDisplayStand;

/**
 * Config for the {@link BlockDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockDisplayStandConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockDisplayStandConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(blockConfig.getResourceKey().location(), ItemModelDisplayStand.Unbaked.MAP_CODEC));
    }
}
