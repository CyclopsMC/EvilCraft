package org.cyclops.evilcraft.block;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.client.render.model.ItemDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.config.extendedconfig.BlockClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.render.model.ModelDarkTankBaked;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author rubensworks
 */
public class BlockDarkTankConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockDarkTankConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
    }

    @Override
    @Nullable
    public IDynamicModelElementCommon getDynamicModelElement() {
        return new DynamicModel();
    }

    public static class DynamicModel implements IDynamicModelElementCommon {

        private ModelDarkTankBaked model;

        @Override
        public BlockStateModel createDynamicBlockModel(Consumer<Pair<BlockState, BlockStateModel>> modelConsumer, Function<BlockState, BlockStateModel> modelRetriever) {
            BlockState blockState = RegistryEntries.BLOCK_DARK_TANK.get().defaultBlockState();
            model = new ModelDarkTankBaked(modelRetriever.apply(blockState));
            modelConsumer.accept(Pair.of(blockState, model));
            return model;
        }

        @Override
        public ItemModel createDynamicItemModel(Consumer<Pair<ResourceLocation, ItemModel>> modelConsumer, Function<ResourceLocation, ItemModel> modelRetriever) {
            ItemDynamicItemAndBlockModel itemModel = new ItemDynamicItemAndBlockModel(model, model.getModelRenderProperties());
            ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(RegistryEntries.BLOCK_DARK_TANK.get());
            modelConsumer.accept(Pair.of(registryName, itemModel));
            return itemModel;
        }
    }
}
