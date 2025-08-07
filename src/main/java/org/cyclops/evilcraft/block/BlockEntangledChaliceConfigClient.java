package org.cyclops.evilcraft.block;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityEntangledChalice;
import org.cyclops.evilcraft.client.render.model.ItemModelEntangledChalice;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChaliceBaked;

/**
 * @author rubensworks
 */
public class BlockEntangledChaliceConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockEntangledChaliceConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener((RegisterSpecialModelRendererEvent event) -> event.register(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entangled_chalice_fluid_overlay"), RenderItemStackBlockEntityEntangledChalice.Unbaked.MAP_CODEC));
        blockConfig.getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(ItemModelEntangledChalice.Unbaked.ID, ItemModelEntangledChalice.Unbaked.MAP_CODEC));
        blockConfig.getMod().getModEventBus().addListener((RegisterBlockStateModels event) -> event.registerModel(ModelEntangledChaliceBaked.Unbaked.ID, ModelEntangledChaliceBaked.Unbaked.CODEC));
    }
}
