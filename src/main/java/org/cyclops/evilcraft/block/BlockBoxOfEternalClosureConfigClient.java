package org.cyclops.evilcraft.block;

import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ItemModelBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;

/**
 * @author rubensworks
 */
public class BlockBoxOfEternalClosureConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockBoxOfEternalClosureConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener((RegisterSpecialModelRendererEvent event) -> event.register(blockConfig.getResourceKey().location(), RenderItemStackBlockEntityBoxOfEternalClosure.Unbaked.MAP_CODEC));
        blockConfig.getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(ItemModelBoxOfEternalClosure.Unbaked.ID, ItemModelBoxOfEternalClosure.Unbaked.MAP_CODEC));
        blockConfig.getMod().getModEventBus().addListener(this::registerBlockStateModel);
    }

    public void registerBlockStateModel(RegisterBlockStateModels event) {
        event.registerModel(ModelBoxOfEternalClosureBaked.Unbaked.ID, ModelBoxOfEternalClosureBaked.Unbaked.CODEC);
    }
}
