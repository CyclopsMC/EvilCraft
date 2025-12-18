package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
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
import java.util.Map;

/**
 * The dynamic item model for the display stand.
 * This works in a very hacky manner, as it stores all information for baking at game-time using ModelLoaderDisplayStand.
 * @author rubensworks
 */
public class ModelDisplayStandBaked extends DynamicItemAndBlockModel {

    private final SingleCache<Material, BlockModelPart> modelCache = new SingleCache<>(new SingleCache.ICacheUpdater<>() {
        @Override
        public BlockModelPart getNewValue(Material key) {
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
    private final TextureAtlasSprite particleIcon;

    public ModelDisplayStandBaked(ModelBaker baker, ResolvedModel resolvedModel, ModelState modelState, TextureSlots textureSlots, TextureAtlasSprite particleIcon) {
        super(true, false);
        this.baker = baker;
        this.resolvedModel = resolvedModel;
        this.modelState = modelState;
        this.textureSlots = textureSlots;
        this.particleIcon = particleIcon;
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return particleIcon;
    }

    @Nullable
    protected BlockModelPart handleDisplayStandType(ItemStack displayStandType) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = IModHelpers.get().getBlockHelpers().getBlockStateFromItemStack(displayStandType);
            TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getBlockModelShaper()
                    .getBlockModel(blockState).particleIcon();
            return modelCache.get(new Material(texture.atlasLocation(), texture.contents().name()));
        }
        return null;
    }

    public BlockModelPart bakeModel(Material material) {
        // Override all textures in the model to the given texture
        Map<String, Material> resolvedValuesOverride = Maps.newHashMap();
        for (Map.Entry<String, Material> entry : textureSlots.resolvedValues.entrySet()) {
            resolvedValuesOverride.put(entry.getKey(), material);
        }
        TextureSlots textureSlotsOverride = new TextureSlots(resolvedValuesOverride);

        // Based on SimpleModelWrapper
        boolean useAmbientOcclusion = resolvedModel.getTopAmbientOcclusion();
        TextureAtlasSprite textureatlassprite = resolvedModel.resolveParticleSprite(textureSlotsOverride, baker);
        QuadCollection quadcollection = resolvedModel.wrapped().geometry().bake(textureSlotsOverride, baker, modelState, resolvedModel, resolvedModel.getTopAdditionalProperties()); // Call .wrapped() to bypass resolvedmodel cache
        var renderTypeGroup = resolvedModel.getTopAdditionalProperties().getOptional(net.neoforged.neoforge.client.model.NeoForgeModelProperties.RENDER_TYPE);
        var renderTypes = renderTypeGroup == null || renderTypeGroup.isEmpty() ? null : renderTypeGroup.block();

        return new SimpleModelWrapper(quadcollection, useAmbientOcclusion, textureatlassprite, renderTypes);
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
        BlockModelPart blockModelPart = handleDisplayStandType(ModelHelpers.getSafeProperty(getModelData(level, pos), BlockDisplayStand.TYPE, ItemStack.EMPTY));
        if (blockModelPart != null) {
            quads.addAll(blockModelPart.getQuads(null));
        }
        return quads;
    }

    @Override
    public List<BakedQuad> handleItemState(@Nullable ItemStack stack, @Nullable Level level, @Nullable ItemOwner entity) {
        List<BakedQuad> quads = Lists.newLinkedList();
        BlockModelPart blockModelPart = handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.get().getDisplayStandType(stack));
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
    public TextureAtlasSprite particleIcon(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        BlockModelPart part = handleDisplayStandType(ModelHelpers.getSafeProperty(getModelData(level, pos), BlockDisplayStand.TYPE, ItemStack.EMPTY));
        if (part != null) {
            return part.particleIcon();
        }
        return particleIcon();
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":display_stand";
    }

    public record Unbaked(ResourceLocation base, Variant.SimpleModelState modelState) implements CustomUnbakedBlockStateModel {

        public static final MapCodec<ModelDisplayStandBaked.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                                ResourceLocation.CODEC.fieldOf("base").forGetter(ModelDisplayStandBaked.Unbaked::base),
                                Variant.SimpleModelState.MAP_CODEC.forGetter(ModelDisplayStandBaked.Unbaked::modelState)
                        )
                        .apply(builder, ModelDisplayStandBaked.Unbaked::new));
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "display_stand");

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.base);
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            ResolvedModel resolvedModel = baker.getModel(base);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            TextureAtlasSprite textureatlassprite = resolvedModel.resolveParticleSprite(textureslots, baker);
            return new ModelDisplayStandBaked(baker, resolvedModel, modelState.asModelState(), textureslots, textureatlassprite);
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return CODEC;
        }
    }
}
