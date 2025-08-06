package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;
import org.cyclops.evilcraft.core.client.model.ModelLoaderDisplayStand;
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

    private static final Map<Direction, BlockModelRotation> FACING_ROTATIONS = ImmutableMap.<Direction, BlockModelRotation>builder()
            .put(Direction.NORTH, BlockModelRotation.X270_Y0)
            .put(Direction.SOUTH, BlockModelRotation.X90_Y0)
            .put(Direction.WEST, BlockModelRotation.X90_Y90)
            .put(Direction.EAST, BlockModelRotation.X270_Y90)
            .put(Direction.UP, BlockModelRotation.X180_Y0)
            .put(Direction.DOWN, BlockModelRotation.X0_Y0)
            .build();
    private static final Map<Boolean, String> AXIS_X_MODELS = ImmutableMap.<Boolean, String>builder()
            .put(true, "evilcraft:block/display_stand")
            .put(false, "evilcraft:block/display_stand_rotated")
            .build();

    private final SingleCache<Triple<Material, Direction, Boolean>, BlockModelPart> modelCache = new SingleCache<>(new SingleCache.ICacheUpdater<>() {
        @Override
        public BlockModelPart getNewValue(Triple<Material, Direction, Boolean> key) {
            return bakeModel(key.getLeft(), key.getMiddle(), key.getRight());
        }

        @Override
        public boolean isKeyEqual(Triple<Material, Direction, Boolean> k1, Triple<Material, Direction, Boolean> k2) {
            return k1.equals(k2);
        }
    });

    public ModelDisplayStandBaked() {
        super(true, false);
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        // TODO: fix breaking particle
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        try {
            return atlas.getSprite(ResourceLocation.withDefaultNamespace("missingno"));
        } catch (IllegalStateException e) {
            return null; // Can happen during game load
        }
    }

    @Nullable
    protected BlockModelPart handleDisplayStandType(ItemStack displayStandType, Direction direction, boolean axisX) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = IModHelpers.get().getBlockHelpers().getBlockStateFromItemStack(displayStandType);
            TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getBlockModelShaper()
                    .getBlockModel(blockState).particleIcon();
            return modelCache.get(Triple.of(new Material(texture.atlasLocation(), texture.contents().name()), direction, axisX));
        }
        return null;
    }

    public static BlockModelPart bakeModel(Material material, Direction direction, boolean axisX) {
        ResolvedModel resolvedmodel = ModelLoaderDisplayStand.getInstance().getResolvedModels().get(AXIS_X_MODELS.get(axisX));
        ModelBaker baker = ModelLoaderDisplayStand.getInstance().getBaker();
        TextureSlots textureSlots = ModelLoaderDisplayStand.getInstance().getTextureSlots();

        // Override all textures in the model to the given texture
        Map<String, Material> resolvedValuesOverride = Maps.newHashMap();
        for (Map.Entry<String, Material> entry : textureSlots.resolvedValues.entrySet()) {
            resolvedValuesOverride.put(entry.getKey(), material);
        }
        TextureSlots textureSlotsOverride = new TextureSlots(resolvedValuesOverride);

        // Based on SimpleModelWrapper
        boolean useAmbientOcclusion = resolvedmodel.getTopAmbientOcclusion();
        TextureAtlasSprite textureatlassprite = resolvedmodel.resolveParticleSprite(textureSlotsOverride, baker);
        QuadCollection quadcollection = resolvedmodel.wrapped().geometry().bake(textureSlotsOverride, baker, FACING_ROTATIONS.get(direction), resolvedmodel, resolvedmodel.getTopAdditionalProperties()); // Call .wrapped() to bypass resolvedmodel cache
        var renderTypeGroup = resolvedmodel.getTopAdditionalProperties().getOptional(net.neoforged.neoforge.client.model.NeoForgeModelProperties.RENDER_TYPE);
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
        BlockModelPart blockModelPart = handleDisplayStandType(ModelHelpers.getSafeProperty(getModelData(level, pos), BlockDisplayStand.TYPE, ItemStack.EMPTY), state.getValue(BlockDisplayStand.FACING), state.getValue(BlockDisplayStand.AXIS_X));
        if (blockModelPart != null) {
            quads.addAll(blockModelPart.getQuads(null));
        }
        return quads;
    }

    @Override
    public List<BakedQuad> handleItemState(@Nullable ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity) {
        List<BakedQuad> quads = Lists.newLinkedList();
        BlockModelPart blockModelPart = handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.get().getDisplayStandType(stack), Direction.DOWN, true);
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
        BlockModelPart part = handleDisplayStandType(ModelHelpers.getSafeProperty(getModelData(level, pos), BlockDisplayStand.TYPE, ItemStack.EMPTY), Direction.DOWN, true);
        if (part != null) {
            return part.particleIcon();
        }
        return particleIcon();
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":display_stand";
    }
}
