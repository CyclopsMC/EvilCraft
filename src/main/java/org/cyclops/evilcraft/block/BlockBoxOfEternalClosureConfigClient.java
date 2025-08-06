package org.cyclops.evilcraft.block;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.client.render.model.ItemDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.blockentity.RenderItemStackBlockEntityBoxOfEternalClosure;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosureBaked;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author rubensworks
 */
public class BlockBoxOfEternalClosureConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockBoxOfEternalClosureConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
        blockConfig.getMod().getModEventBus().addListener((RegisterSpecialModelRendererEvent event) -> event.register(blockConfig.getResourceKey().location(), RenderItemStackBlockEntityBoxOfEternalClosure.Unbaked.MAP_CODEC));
    }

    @Override
    @Nullable
    public IDynamicModelElementCommon getDynamicModelElement() {
        return new BlockBoxOfEternalClosureConfigClient.DynamicModel();
    }

    public static class DynamicModel implements IDynamicModelElementCommon {

        private ModelBoxOfEternalClosureBaked model;

        @Override
        public BlockStateModel createDynamicBlockModel(Consumer<Pair<BlockState, BlockStateModel>> modelConsumer, Function<BlockState, BlockStateModel> modelRetriever) {
            BlockState blockState = RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get().defaultBlockState();
            model = new ModelBoxOfEternalClosureBaked();
            for (Direction facing : BlockBoxOfEternalClosure.FACING.getPossibleValues()) {
                modelConsumer.accept(Pair.of(blockState
                        .setValue(BlockBoxOfEternalClosure.FACING, facing), model));
            }
            return model;
        }

        @Override
        public ItemModel createDynamicItemModel(Consumer<Pair<ResourceLocation, ItemModel>> modelConsumer, Function<ResourceLocation, ItemModel> modelRetriever) {
            ItemDynamicItemAndBlockModel itemModel = new ItemDynamicItemAndBlockModel(model, model.getModelRenderProperties());
            ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get());
            modelConsumer.accept(Pair.of(registryName, itemModel));
            return itemModel;
        }
    }
}
