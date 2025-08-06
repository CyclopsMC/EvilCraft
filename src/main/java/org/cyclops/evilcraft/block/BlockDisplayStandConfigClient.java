package org.cyclops.evilcraft.block;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Direction;
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
import org.cyclops.evilcraft.client.render.model.ModelDisplayStandBaked;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Config for the {@link BlockDisplayStand}.
 * @author rubensworks
 *
 */
public class BlockDisplayStandConfigClient extends BlockClientConfig<ModBaseNeoForge<?>> {
    public BlockDisplayStandConfigClient(BlockConfigCommon<ModBaseNeoForge<?>> blockConfig) {
        super(blockConfig);
    }

    @Override
    @Nullable
    public IDynamicModelElementCommon getDynamicModelElement() {
        return new BlockDisplayStandConfigClient.DynamicModel();
    }

    public static class DynamicModel implements IDynamicModelElementCommon {

        private ModelDisplayStandBaked model;

        @Override
        public BlockStateModel createDynamicBlockModel(Consumer<Pair<BlockState, BlockStateModel>> modelConsumer, Function<BlockState, BlockStateModel> modelRetriever) {
            BlockState blockState = RegistryEntries.BLOCK_DISPLAY_STAND.get().defaultBlockState();
            model = new ModelDisplayStandBaked();
            for (Direction facing : BlockDisplayStand.FACING.getPossibleValues()) {
                for (boolean axisX : BlockDisplayStand.AXIS_X.getPossibleValues()) {
                    modelConsumer.accept(Pair.of(blockState
                            .setValue(BlockDisplayStand.FACING, facing)
                            .setValue(BlockDisplayStand.AXIS_X, axisX), model));
                }
            }
            return model;
        }

        @Override
        public ItemModel createDynamicItemModel(Consumer<Pair<ResourceLocation, ItemModel>> modelConsumer, Function<ResourceLocation, ItemModel> modelRetriever) {
            ItemDynamicItemAndBlockModel itemModel = new ItemDynamicItemAndBlockModel(model, model.getModelRenderProperties());
            ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(RegistryEntries.BLOCK_DISPLAY_STAND.get());
            modelConsumer.accept(Pair.of(registryName, itemModel));
            return itemModel;
        }
    }
}
