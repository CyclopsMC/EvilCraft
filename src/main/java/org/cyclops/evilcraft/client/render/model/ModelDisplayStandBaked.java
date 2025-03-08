package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * The dynamic item model for the display stand.
 * Inspired by TCon's dynamic tool table retexturing.
 * @author rubensworks
 */
public class ModelDisplayStandBaked extends DynamicItemAndBlockModel {

    private static final Map<Direction, BlockModelRotation> ROTATIONS = ImmutableMap.<Direction, BlockModelRotation>builder()
            .put(Direction.NORTH, BlockModelRotation.X270_Y0)
            .put(Direction.SOUTH, BlockModelRotation.X90_Y0)
            .put(Direction.WEST, BlockModelRotation.X90_Y90)
            .put(Direction.EAST, BlockModelRotation.X270_Y90)
            .put(Direction.UP, BlockModelRotation.X180_Y0)
            .put(Direction.DOWN, BlockModelRotation.X0_Y0)
            .build();

    private final SingleCache<Pair<Material, ItemTransforms>, BakedModel> modelCache = new SingleCache<>(new SingleCache.ICacheUpdater<Pair<Material, ItemTransforms>, BakedModel>() {
        @Override
        public BakedModel getNewValue(Pair<Material, ItemTransforms> key) {
            return bakeModel(blockModel, textureSlots, untexturedBakedModel, key.getKey(), baker, transform, key.getRight());
        }

        @Override
        public boolean isKeyEqual(Pair<Material, ItemTransforms> k1, Pair<Material, ItemTransforms> k2) {
            return k1.getLeft().equals(k2.getLeft());
        }
    });

    private final BlockModel blockModel;
    private final TextureSlots textureSlots;
    private final BakedModel untexturedBakedModel;
    private final Material material;
    private final ModelBaker baker;
    private final ModelState transform;

    public ModelDisplayStandBaked(BlockModel blockModel, TextureSlots textureSlots, BakedModel untexturedBakedModel, ModelBaker baker, ModelState transform) {
        super(true, false);
        this.blockModel = blockModel;
        this.textureSlots = textureSlots;
        this.untexturedBakedModel = untexturedBakedModel;
        this.baker = baker;
        this.transform = transform;
        this.material = null;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.material.sprite();
    }

    protected BakedModel handleDisplayStandType(ItemStack displayStandType, ItemTransforms itemTransforms) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = IModHelpers.get().getBlockHelpers().getBlockStateFromItemStack(displayStandType);
            TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getBlockModelShaper()
                    .getBlockModel(blockState).getParticleIcon();
            return modelCache.get(Pair.of(new Material(texture.atlasLocation(), texture.contents().name()), itemTransforms));
        }
        return untexturedBakedModel;
    }

    public static BakedModel bakeModel(BlockModel blockModel, TextureSlots textureSlots, BakedModel untexturedBakedModel, Material material, ModelBaker baker, ModelState modelState, ItemTransforms itemTransforms) {
        // Override all textures in the model to the given texture
        Map<String, Material> resolvedValuesOverride = Maps.newHashMap();
        for (Map.Entry<String, Material> entry : textureSlots.resolvedValues.entrySet()) {
            resolvedValuesOverride.put(entry.getKey(), material);
        }
        TextureSlots textureSlotsOverride = new TextureSlots(resolvedValuesOverride);

        return blockModel.bake(textureSlotsOverride, baker, modelState, untexturedBakedModel.useAmbientOcclusion(), untexturedBakedModel.usesBlockLight(), itemTransforms, ContextMap.EMPTY);
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
    public BakedModel handleBlockState(BlockState state, Direction side, RandomSource rand, ModelData modelData, RenderType renderType) {
        return handleDisplayStandType(ModelHelpers.getSafeProperty(modelData, BlockDisplayStand.TYPE, ItemStack.EMPTY), getTransforms());
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        throw new UnsupportedOperationException("handleItemState is not supported with these parameters");
    }

    public BakedModel handleItemState(ItemStack itemStack, ItemTransforms itemTransforms) {
        return handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.get().getDisplayStandType(itemStack), itemTransforms);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        throw new UnsupportedOperationException("getGeneralQuads is not supported in a factory");
    }

    @Override
    public boolean usesBlockLight() {
        return true ; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public ItemTransforms getTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull ModelData data) {
        return handleDisplayStandType(ModelHelpers.getSafeProperty(data, BlockDisplayStand.TYPE, ItemStack.EMPTY), getTransforms()).getParticleIcon();
    }
}
