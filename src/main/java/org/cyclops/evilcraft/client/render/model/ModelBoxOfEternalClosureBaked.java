package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A baked boec model.
 *
 * @author rubensworks
 */
public class ModelBoxOfEternalClosureBaked extends DelegatingDynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ItemTransforms TRANSFORMS = ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
            ItemDisplayContext.GUI, new ItemTransform(
                    new Vector3f(30, 135, 0),
                    new Vector3f(0, 0, 0),
                    new Vector3f(0.625f, 0.625f, 0.625f))
    ));

    private final BlockStateModel boxModel;
    private final BlockStateModel boxLidModel;
    private final BlockStateModel boxLidRotatedModel;

    private final boolean isOpen;

    public ModelBoxOfEternalClosureBaked(BlockStateModel boxModel, BlockStateModel boxLidModel, BlockStateModel boxLidRotatedModel, boolean isOpen) {
        super();
        this.isOpen = isOpen;
        this.boxModel = boxModel;
        this.boxLidModel = boxLidModel;
        this.boxLidRotatedModel = boxLidRotatedModel;
    }

    public BlockStateModel getBoxModel() {
        return boxModel;
    }

    public BlockStateModel getBoxLidModel() {
        return boxLidModel;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads = Lists.newLinkedList();

        for (BlockModelPart blockModelPart : boxModel.collectParts(level, BlockPos.ZERO, blockState, rand)) {
            quads.addAll(blockModelPart.getQuads(null));
        }
        if (isOpen) {
            for (BlockModelPart blockModelPart : boxLidRotatedModel.collectParts(level, BlockPos.ZERO, blockState, rand)) {
                quads.addAll(blockModelPart.getQuads(null));
            }
        } else {
            for (BlockModelPart blockModelPart : boxLidModel.collectParts(level, BlockPos.ZERO, blockState, rand)) {
                quads.addAll(blockModelPart.getQuads(null));
            }
        }

        return quads;
    }

    @Override
    public List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction side, RandomSource rand, ModelData extraData, ChunkSectionLayer renderType) {
        return getGeneralQuads();
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData tileData) {
        return null;
    }

    @Override
    public List<ChunkSectionLayer> getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return List.of();
    }

    @Override
    public List<BakedQuad> handleItemState(@Nullable ItemStack itemStack, @Nullable Level world,
                                           @Nullable LivingEntity entity) {
        return new ModelBoxOfEternalClosureBaked(this.boxModel, this.boxLidModel, this.boxLidRotatedModel, BlockBoxOfEternalClosure.getSpiritTypeWithFallbackSpirit(itemStack) == null).getGeneralQuads();
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return this.boxModel.particleIcon();
    }

    @Override
    public boolean usesBlockLight() {
        return false; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public UnbakedModel wrapped() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable ResolvedModel parent() {
        return null;
    }

    @Override
    public ItemTransforms getTopTransforms() {
        return TRANSFORMS;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public String toString() {
        return "ModelBoxOfEternalClosureBaked(isOpen=" + this.isOpen() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ModelBoxOfEternalClosureBaked)) return false;
        final ModelBoxOfEternalClosureBaked other = (ModelBoxOfEternalClosureBaked) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isOpen() != other.isOpen()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ModelBoxOfEternalClosureBaked;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isOpen() ? 79 : 97);
        return result;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":box_of_eternal_closure";
    }

    public record Unbaked(ResourceLocation box, ResourceLocation boxLid, ResourceLocation boxLidRotated, Variant.SimpleModelState modelState) implements CustomUnbakedBlockStateModel {

        public static final MapCodec<ModelBoxOfEternalClosureBaked.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                                ResourceLocation.CODEC.fieldOf("box").forGetter(ModelBoxOfEternalClosureBaked.Unbaked::box),
                                ResourceLocation.CODEC.fieldOf("box_lid").forGetter(ModelBoxOfEternalClosureBaked.Unbaked::boxLid),
                                ResourceLocation.CODEC.fieldOf("box_lid_rotated").forGetter(ModelBoxOfEternalClosureBaked.Unbaked::boxLidRotated),
                                Variant.SimpleModelState.MAP_CODEC.forGetter(Unbaked::modelState)
                        )
                        .apply(builder, ModelBoxOfEternalClosureBaked.Unbaked::new));
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "box_of_eternal_closure");

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.box);
            resolver.markDependency(this.boxLid);
            resolver.markDependency(this.boxLidRotated);
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            ModelState modelState = this.modelState.asModelState();
            return new ModelBoxOfEternalClosureBaked(
                    ModelHelpers.bakeSingleBlockStateModel(baker, box, modelState),
                    ModelHelpers.bakeSingleBlockStateModel(baker, boxLid, modelState),
                    ModelHelpers.bakeSingleBlockStateModel(baker, boxLidRotated, modelState),
                    false
            );
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return CODEC;
        }
    }
}
