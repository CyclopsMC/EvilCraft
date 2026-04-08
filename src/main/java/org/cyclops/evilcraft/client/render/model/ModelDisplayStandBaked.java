package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The dynamic item model for the display stand.
 * This works in a very hacky manner, as it stores all information for baking at game-time using ModelLoaderDisplayStand.
 * @author rubensworks
 */
public class ModelDisplayStandBaked extends DynamicItemAndBlockModel {

    private final SingleCache<Material, BlockStateModelPart> modelCache = new SingleCache<>(new SingleCache.ICacheUpdater<>() {
        @Override
        public BlockStateModelPart getNewValue(Material key) {
            return bakeModel(key);
        }

        @Override
        public boolean isKeyEqual(Material k1, Material k2) {
            return k1.equals(k2);
        }
    });

    private final ModelBaker baker;
    private final ResolvedModel resolvedModel;
    private final ModelState modelState;
    private final TextureSlots textureSlots;
    private final Material.Baked particleMaterialBaked;

    public ModelDisplayStandBaked(ModelBaker baker, ResolvedModel resolvedModel, ModelState modelState, TextureSlots textureSlots, Material.Baked particleMaterialBaked) {
        super(true, false);
        this.baker = baker;
        this.resolvedModel = resolvedModel;
        this.modelState = modelState;
        this.textureSlots = textureSlots;
        this.particleMaterialBaked = particleMaterialBaked;
    }

    @Override
    public Material.Baked particleMaterial() {
        return particleMaterialBaked;
    }

    @Override
    public int materialFlags() {
        return 0;
    }

    @Nullable
    protected BlockStateModelPart handleDisplayStandType(ItemStack displayStandType) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = IModHelpers.get().getBlockHelpers().getBlockStateFromItemStack(displayStandType);
            BlockStateModel blockModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState);
            Material.Baked blockParticleMaterial = blockModel.particleMaterial();
            if (blockParticleMaterial != null) {
                return modelCache.get(new Material(blockParticleMaterial.sprite().contents().name()));
            }
        }
        return null;
    }

    public BlockStateModelPart bakeModel(Material material) {
        // Override all textures in the model to the given texture
        // Get the original texture slot names from the wrapped model
        TextureSlots.Data originalData = resolvedModel.wrapped().textureSlots();
        TextureSlots.Data.Builder overrideBuilder = new TextureSlots.Data.Builder();
        for (String slotName : originalData.values().keySet()) {
            overrideBuilder.addTexture(slotName, material);
        }
        TextureSlots textureSlotsOverride = new TextureSlots.Resolver()
                .addFirst(overrideBuilder.build())
                .addLast(originalData)
                .resolve(resolvedModel);

        // Based on SimpleModelWrapper
        boolean useAmbientOcclusion = resolvedModel.getTopAmbientOcclusion();
        Material.Baked particleMatBaked = resolvedModel.resolveParticleMaterial(textureSlotsOverride, baker);
        QuadCollection quadcollection = resolvedModel.bakeTopGeometry(textureSlotsOverride, baker, modelState);

        return new SimpleModelWrapper(quadcollection, useAmbientOcclusion, particleMatBaked);
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData) {
        return IModHelpers.get().getBlockEntityHelpers().get(world, pos, BlockEntityDisplayStand.class)
                .map(tile -> {
                    ModelData.Builder builder = ModelData.builder();
                    builder.with(BlockDisplayStand.DIRECTION, tile.getDirection());
                    builder.with(BlockDisplayStand.TYPE, tile.getDisplayStandType());
                    return builder.build();
                })
                .orElse(ModelData.EMPTY);
    }

    @Override
    public List<ChunkSectionLayer> getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return List.of(ChunkSectionLayer.values());
    }

    @Override
    public List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction side, RandomSource rand, ModelData extraData, ChunkSectionLayer renderType) {
        List<BakedQuad> quads = Lists.newLinkedList();
        BlockStateModelPart blockModelPart = handleDisplayStandType(ModelHelpers.getSafeProperty(getModelData(level, pos), BlockDisplayStand.TYPE, ItemStack.EMPTY));
        if (blockModelPart != null) {
            quads.addAll(blockModelPart.getQuads(null));
        }
        return quads;
    }

    @Override
    public List<BakedQuad> handleItemState(@Nullable ItemStack stack, @Nullable Level level, @Nullable ItemOwner entity) {
        List<BakedQuad> quads = Lists.newLinkedList();
        BlockStateModelPart blockModelPart = handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.get().getDisplayStandType(stack));
        if (blockModelPart != null) {
            quads.addAll(blockModelPart.getQuads(null));
        }
        return quads;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        throw new UnsupportedOperationException("getGeneralQuads is not supported in a factory");
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public UnbakedModel wrapped() {
        return null;
    }

    @Override
    public @Nullable ResolvedModel parent() {
        return null;
    }

    @Override
    public ItemTransforms getTopTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos) {
        return level.getBlockEntity(pos, RegistryEntries.BLOCK_ENTITY_DISPLAY_STAND.get())
                .map(tile -> {
                    ModelData.Builder builder = ModelData.builder();
                    builder.with(BlockDisplayStand.DIRECTION, tile.getDirection());
                    builder.with(BlockDisplayStand.TYPE, tile.getDisplayStandType());
                    return builder.build();
                })
                .orElse(ModelData.EMPTY);
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        BlockStateModelPart part = handleDisplayStandType(ModelHelpers.getSafeProperty(getModelData(level, pos), BlockDisplayStand.TYPE, ItemStack.EMPTY));
        if (part != null) {
            return part.particleMaterial();
        }
        return particleMaterial();
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":display_stand";
    }

    public record Unbaked(Identifier base, Variant.SimpleModelState modelState) implements CustomUnbakedBlockStateModel {

        public static final MapCodec<ModelDisplayStandBaked.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                                Identifier.CODEC.fieldOf("base").forGetter(ModelDisplayStandBaked.Unbaked::base),
                                Variant.SimpleModelState.MAP_CODEC.forGetter(ModelDisplayStandBaked.Unbaked::modelState)
                        )
                        .apply(builder, ModelDisplayStandBaked.Unbaked::new));
        public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "display_stand");

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.base);
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            ResolvedModel resolvedModel = baker.getModel(base);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            Material.Baked particleMatBaked = resolvedModel.resolveParticleMaterial(textureslots, baker);
            return new ModelDisplayStandBaked(baker, resolvedModel, modelState.asModelState(), textureslots, particleMatBaked);
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return CODEC;
        }
    }
}
