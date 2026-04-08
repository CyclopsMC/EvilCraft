package org.cyclops.evilcraft.client.render.model;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.ModelState;
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
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemModelBoxOfEternalClosure(ModelBoxOfEternalClosureBaked model, ModelRenderProperties modelRenderProperties) implements ItemModel {

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

    public static record Unbaked(Identifier box, Identifier boxLid, Identifier boxLidRotated) implements ItemModel.Unbaked {
        public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "box_of_eternal_closure");
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Identifier.CODEC.fieldOf("box").forGetter(ItemModelBoxOfEternalClosure.Unbaked::box),
                                Identifier.CODEC.fieldOf("box_lid").forGetter(ItemModelBoxOfEternalClosure.Unbaked::boxLid),
                                Identifier.CODEC.fieldOf("box_lid_rotated").forGetter(ItemModelBoxOfEternalClosure.Unbaked::boxLidRotated)
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
            ResolvedModel resolvedModel = baker.getModel(this.box);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            ModelRenderProperties modelRenderProperties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureslots);
            return new ItemModelBoxOfEternalClosure(bakeBoxModel(baker, BlockModelRotation.get(Quadrant.fromXYAngles(Quadrant.R0, Quadrant.R180))), modelRenderProperties);
        }

        public ModelBoxOfEternalClosureBaked bakeBoxModel(ModelBaker baker, ModelState modelState) {
            return new ModelBoxOfEternalClosureBaked(
                    ModelHelpers.bakeSingleBlockStateModel(baker, box, modelState),
                    ModelHelpers.bakeSingleBlockStateModel(baker, boxLid, modelState),
                    ModelHelpers.bakeSingleBlockStateModel(baker, boxLidRotated, modelState),
                    false
            );
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(box);
            resolver.markDependency(boxLid);
            resolver.markDependency(boxLidRotated);
        }
    }
}
