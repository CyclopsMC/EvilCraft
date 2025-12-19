package org.cyclops.evilcraft.block;

import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityBloodChest;

/**
 * @author rubensworks
 */
public class BlockBloodChestConfigClient extends BlockClientConfig<ModBaseNeoForge<?>>  {
    public BlockBloodChestConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener((RegisterSpecialModelRendererEvent event) -> event.register(blockConfig.getResourceKey().identifier(), RenderItemStackBlockEntityBloodChest.Unbaked.MAP_CODEC));
    }
}
