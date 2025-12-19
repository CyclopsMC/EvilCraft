package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Maps;
import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.BlockModelRotation;
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

import java.util.List;
import java.util.Map;

/**
 * @author rubensworks
 */
public record ItemModelBroomPart(List<ItemTintSource> tints, ModelBroomPartBaked model, ModelRenderProperties modelRenderProperties) implements ItemModel {

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner entity, int seed) {
        List<BakedQuad> quads = model.handleItemState(stack, level, entity);
        new BlockModelWrapper(tints, quads, modelRenderProperties, BlockModelWrapper.detectRenderType(quads))
                .update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }

    public static record Unbaked(List<ItemTintSource> tints) implements ItemModel.Unbaked {
        public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "broom_part");
        public static final MapCodec<ItemModelBroomPart.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                                ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(ItemModelBroomPart.Unbaked::tints)
                        )
                        .apply(builder, ItemModelBroomPart.Unbaked::new)
        );

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext) {
            ModelBaker baker = bakingContext.blockModelBaker();
            Map<IBroomPart, BlockStateModel> broomPartModels = Maps.newIdentityHashMap();
            for(IBroomPart part : BroomParts.REGISTRY.getParts()) {
                broomPartModels.put(part, ModelHelpers.bakeSingleBlockStateModel(baker, BroomParts.REGISTRY.getPartModel(part), BlockModelRotation.get(OctahedralGroup.IDENTITY)));
            }
            return new ItemModelBroomPart(this.tints, new ModelBroomPartBaked(broomPartModels), new ModelRenderProperties(false, null, ModelHelpers.DEFAULT_CAMERA_TRANSFORMS));
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            for (Identifier partModel : BroomParts.REGISTRY.getPartModels()) {
                resolver.markDependency(partModel);
            }
        }
    }
}
