package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Maps;
import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

/**
 * @author rubensworks
 */
public record ItemModelBroom(ModelBroomBaked model, ModelRenderProperties modelRenderProperties) implements ItemModel {

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

    public static record Unbaked() implements ItemModel.Unbaked {
        public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "broom");
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext, org.joml.Matrix4fc matrix4fc) {
            ModelBaker baker = bakingContext.blockModelBaker();
            Map<IBroomPart, BlockStateModel> broomPartModels = Maps.newIdentityHashMap();
            for(IBroomPart part : BroomParts.REGISTRY.getParts()) {
                broomPartModels.put(part, ModelHelpers.bakeSingleBlockStateModel(baker, BroomParts.REGISTRY.getPartModel(part), BlockModelRotation.get(OctahedralGroup.IDENTITY)));
            }
            return new ItemModelBroom(new ModelBroomBaked(broomPartModels), new ModelRenderProperties(false, null, ModelHelpers.DEFAULT_CAMERA_TRANSFORMS));
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            for (Identifier partModel : BroomParts.REGISTRY.getPartModels()) {
                resolver.markDependency(partModel);
            }
        }
    }
}
