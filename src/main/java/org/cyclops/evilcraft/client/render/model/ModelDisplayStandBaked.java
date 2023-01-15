package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;
import org.cyclops.evilcraft.core.client.model.GeometryBakingContextRetextured;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final SingleCache<ResourceLocation, BakedModel> modelCache = new SingleCache<>(new SingleCache.ICacheUpdater<ResourceLocation, BakedModel>() {
        @Override
        public BakedModel getNewValue(ResourceLocation textureName) {
            return bakeModel(blockModel, new GeometryBakingContextRetextured(context, textureName), blockModel.getElements(), transform,
                    ItemOverrides.EMPTY, spriteGetter, new ResourceLocation(Reference.MOD_ID, "dummy"));
        }

        @Override
        public boolean isKeyEqual(ResourceLocation resourceLocation, ResourceLocation k1) {
            return resourceLocation.equals(k1);
        }
    });

    private final BlockModel blockModel;
    private final BakedModel untexturedBakedModel;
    private final TextureAtlasSprite texture;
    private final IGeometryBakingContext context;
    private final ModelState transform;
    private final Function<Material, TextureAtlasSprite> spriteGetter;

    public ModelDisplayStandBaked(BlockModel blockModel, BakedModel untexturedBakedModel, IGeometryBakingContext context, ModelState transform, Function<Material, TextureAtlasSprite> spriteGetter) {
        super(true, false);
        this.blockModel = blockModel;
        this.untexturedBakedModel = untexturedBakedModel;
        this.context = context;
        this.transform = transform;
        this.texture = null;
        this.spriteGetter = spriteGetter;
    }

    public ModelDisplayStandBaked(BlockModel blockModel, BakedModel untexturedBakedModel, TextureAtlasSprite texture, boolean item, IGeometryBakingContext context, ModelState transform, Function<Material, TextureAtlasSprite> spriteGetter) {
        super(false, item);
        this.blockModel = blockModel;
        this.untexturedBakedModel = untexturedBakedModel;
        this.texture = texture;
        this.context = context;
        this.transform = transform;
        this.spriteGetter = spriteGetter;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.texture;
    }

    protected BakedModel handleDisplayStandType(ItemStack displayStandType, boolean item) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = BlockHelpers.getBlockStateFromItemStack(displayStandType);
            ResourceLocation textureName = Minecraft.getInstance().getModelManager().getBlockModelShaper()
                    .getBlockModel(blockState).getParticleIcon().atlasLocation();
            return modelCache.get(textureName);
        }
        return untexturedBakedModel;
    }

    // Inspired by TCon's TableModel, SimpleBlockModel and RetexturedModel
    public static BakedModel bakeModel(BlockModel blockModel, IGeometryBakingContext context, List<BlockElement> blockParts, ModelState transform,
                                        ItemOverrides overrides, Function<Material, TextureAtlasSprite> spriteGetter,
                                        ResourceLocation modelName) {
        TextureAtlasSprite particle = spriteGetter.apply(context.getMaterial("particle"));
        SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(blockModel, overrides, true).particle(particle);
        for(BlockElement blockPart : blockParts) {
            for(Direction direction : blockPart.faces.keySet()) {
                BlockElementFace blockPartFace = blockPart.faces.get(direction);

                // Remove mandatory hash at start of texture name
                String texture = blockPartFace.texture;
                if (texture.charAt(0) == '#') {
                    texture = texture.substring(1);
                }
                TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(texture));

                if (blockPartFace.cullForDirection == null) {
                    builder.addUnculledFace(UnbakedGeometryHelper.bakeElementFace(blockPart, blockPartFace, sprite, direction, transform, modelName));
                } else {
                    builder.addCulledFace(Direction.rotate(transform.getRotation().getMatrix(), blockPartFace.cullForDirection),
                            UnbakedGeometryHelper.bakeElementFace(blockPart, blockPartFace, sprite, direction, transform, modelName));
                }
            }
        }
        return builder.build();
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData) {
        return BlockEntityHelpers.get(world, pos, BlockEntityDisplayStand.class)
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
        return handleDisplayStandType(ModelHelpers.getSafeProperty(modelData, BlockDisplayStand.TYPE, ItemStack.EMPTY), false);
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        return handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.getDisplayStandType(itemStack), true);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.untexturedBakedModel.getQuads(null, null, null)
                .stream()
                .map(quad -> new BakedQuad(quad.getVertices(), quad.getTintIndex(), quad.getDirection(), this.texture, quad.isShade()))
                .collect(Collectors.toList());
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
        return handleDisplayStandType(ModelHelpers.getSafeProperty(data, BlockDisplayStand.TYPE, ItemStack.EMPTY), false)
                .getParticleIcon();
    }
}
