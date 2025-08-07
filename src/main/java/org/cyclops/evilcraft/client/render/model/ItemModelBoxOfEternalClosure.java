package org.cyclops.evilcraft.client.render.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author rubensworks
 */
public record ItemModelBoxOfEternalClosure(Map<ItemDisplayContext, ModelBoxOfEternalClosureBaked> models, ModelRenderProperties modelRenderProperties) implements ItemModel {

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        new BlockModelWrapper(List.of(), models.get(displayContext).handleItemState(stack, level, entity), modelRenderProperties)
                .update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }

    public static record Unbaked(ResourceLocation box, ResourceLocation boxLid, ResourceLocation boxLidRotated) implements ItemModel.Unbaked {
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "box_of_eternal_closure");
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ResourceLocation.CODEC.fieldOf("box").forGetter(ItemModelBoxOfEternalClosure.Unbaked::box),
                                ResourceLocation.CODEC.fieldOf("box_lid").forGetter(ItemModelBoxOfEternalClosure.Unbaked::boxLid),
                                ResourceLocation.CODEC.fieldOf("box_lid_rotated").forGetter(ItemModelBoxOfEternalClosure.Unbaked::boxLidRotated)
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
            ResolvedModel resolvedModel = baker.getModel(this.box);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            ModelRenderProperties modelRenderProperties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureslots);
            Map<ItemDisplayContext, ModelBoxOfEternalClosureBaked> models = new EnumMap<>(ItemDisplayContext.class);
            for (ItemDisplayContext key : ItemDisplayContext.values()) {
                models.put(key, bakeBoxModel(baker, ModelHelpers.DEFAULT_MODEL_STATES.get(key)));
            }
            return new ItemModelBoxOfEternalClosure(models, modelRenderProperties);
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
