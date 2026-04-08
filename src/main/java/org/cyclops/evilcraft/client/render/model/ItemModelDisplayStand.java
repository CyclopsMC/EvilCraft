package org.cyclops.evilcraft.client.render.model;

import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.Reference;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemModelDisplayStand(ModelDisplayStandBaked model, ModelRenderProperties modelRenderProperties) implements ItemModel {

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner entity, int seed) {
        List<BakedQuad> quads = model.handleItemState(stack, level, entity);
        QuadCollection.Builder quadBuilder = new QuadCollection.Builder();
        for (BakedQuad quad : quads) {
            quadBuilder.addUnculledFace(quad);
        }
        new CuboidItemModelWrapper(List.of(), quadBuilder.build(), modelRenderProperties, new Matrix4f())
                .update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }

    public static record Unbaked(Identifier base) implements ItemModel.Unbaked {
        public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "display_stand");
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Identifier.CODEC.fieldOf("base").forGetter(ItemModelDisplayStand.Unbaked::base)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext, org.joml.Matrix4fc matrix4fc) {
            ModelBaker baker = bakingContext.blockModelBaker();
            ResolvedModel resolvedModel = baker.getModel(this.base);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            ModelRenderProperties modelRenderProperties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureslots);
            ModelDisplayStandBaked model = new ModelDisplayStandBaked(
                    baker,
                    resolvedModel,
                    BlockModelRotation.get(OctahedralGroup.IDENTITY),
                    textureslots,
                    null
            );
            return new ItemModelDisplayStand(model, modelRenderProperties);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(base);
        }
    }
}
