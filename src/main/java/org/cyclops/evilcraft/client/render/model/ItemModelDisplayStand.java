package org.cyclops.evilcraft.client.render.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemModelDisplayStand(ModelDisplayStandBaked model, ItemTransforms itemTransforms) implements ItemModel {
    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        new BlockModelWrapper(this.model.handleItemState(stack, itemTransforms), List.of(new Constant(-1)))
                .update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }

    public static record Unbaked(ResourceLocation model) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ResourceLocation.CODEC.fieldOf("model").forGetter(Unbaked::model)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext) {
            // Source ItemTransforms from the base block model, similar to DynamicFluidContainerModel
            var baseItemModel = bakingContext.blockModelBaker().getModel(ResourceLocation.withDefaultNamespace("block/block"));
            if (baseItemModel == null) {
                throw new IllegalStateException("Failed to access block/block model");
            }

            return new ItemModelDisplayStand((ModelDisplayStandBaked) bakingContext.bake(model), baseItemModel.getTransforms());
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.resolve(model);
        }
    }
}
