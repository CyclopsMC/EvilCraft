package org.cyclops.evilcraft.client.render.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemModelEntangledChalice(ModelEntangledChaliceBaked model, ModelRenderProperties modelRenderProperties) implements ItemModel {

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner entity, int seed) {
        new BlockModelWrapper(List.of(), model.handleItemState(stack, level, entity), modelRenderProperties)
                .update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }

    public static record Unbaked(ResourceLocation chalice, ResourceLocation gems) implements ItemModel.Unbaked {
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entangled_chalice");
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ResourceLocation.CODEC.fieldOf("chalice").forGetter(ItemModelEntangledChalice.Unbaked::chalice),
                                ResourceLocation.CODEC.fieldOf("gems").forGetter(ItemModelEntangledChalice.Unbaked::gems)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext) {
            ModelBaker baker = bakingContext.blockModelBaker();
            ResolvedModel resolvedModel = baker.getModel(chalice);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            ModelRenderProperties modelRenderProperties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureslots);
            ModelEntangledChaliceBaked model = new ModelEntangledChaliceBaked(
                    ModelHelpers.bakeSingleBlockStateModel(baker, chalice, BlockModelRotation.X0_Y0),
                    ModelHelpers.bakeSingleBlockStateModel(baker, gems, BlockModelRotation.X0_Y0)
            );
            return new ItemModelEntangledChalice(model, modelRenderProperties);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(chalice);
            resolver.markDependency(gems);
        }
    }
}
