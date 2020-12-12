package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.core.client.model.ModelConfigurationRetextured;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The dynamic item model for the display stand.
 * Inspired by TCon's dynamic tool table retexturing.
 * @author rubensworks
 */
public class ModelDisplayStandBaked extends DynamicItemAndBlockModel {

    private static final Map<Direction, ModelRotation> ROTATIONS = ImmutableMap.<Direction, ModelRotation>builder()
            .put(Direction.NORTH, ModelRotation.X270_Y0)
            .put(Direction.SOUTH, ModelRotation.X90_Y0)
            .put(Direction.WEST, ModelRotation.X90_Y90)
            .put(Direction.EAST, ModelRotation.X270_Y90)
            .put(Direction.UP, ModelRotation.X180_Y0)
            .put(Direction.DOWN, ModelRotation.X0_Y0)
            .build();

    private final SingleCache<ResourceLocation, IBakedModel> modelCache = new SingleCache<>(new SingleCache.ICacheUpdater<ResourceLocation, IBakedModel>() {
        @Override
        public IBakedModel getNewValue(ResourceLocation textureName) {
            return bakeModel(new ModelConfigurationRetextured(owner, textureName), blockModel.getElements(), transform,
                    ItemOverrideList.EMPTY, ModelLoader.defaultTextureGetter(), new ResourceLocation(Reference.MOD_ID, "dummy"));
        }

        @Override
        public boolean isKeyEqual(ResourceLocation resourceLocation, ResourceLocation k1) {
            return resourceLocation.equals(k1);
        }
    });

    private final BlockModel blockModel;
    private final IBakedModel untexturedBakedModel;
    private final TextureAtlasSprite texture;
    private final IModelConfiguration owner;
    private final IModelTransform transform;

    public ModelDisplayStandBaked(BlockModel blockModel, IBakedModel untexturedBakedModel, IModelConfiguration owner, IModelTransform transform) {
        super(true, false);
        this.blockModel = blockModel;
        this.untexturedBakedModel = untexturedBakedModel;
        this.owner = owner;
        this.transform = transform;
        this.texture = null;
    }

    public ModelDisplayStandBaked(BlockModel blockModel, IBakedModel untexturedBakedModel, TextureAtlasSprite texture, boolean item, IModelConfiguration owner, IModelTransform transform) {
        super(false, item);
        this.blockModel = blockModel;
        this.untexturedBakedModel = untexturedBakedModel;
        this.texture = texture;
        this.owner = owner;
        this.transform = transform;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.texture;
    }

    protected IBakedModel handleDisplayStandType(ItemStack displayStandType, boolean item) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = BlockHelpers.getBlockStateFromItemStack(displayStandType);
            ResourceLocation textureName = Minecraft.getInstance().getModelManager().getBlockModelShapes()
                    .getModel(blockState).getParticleTexture().getName();
            return modelCache.get(textureName);
        }
        return untexturedBakedModel;
    }

    // Inspired by TCon's TableModel, SimpleBlockModel and RetexturedModel
    public static IBakedModel bakeModel(IModelConfiguration modelConfiguration, List<BlockPart> blockParts, IModelTransform transform,
                                        ItemOverrideList overrides, Function<Material, TextureAtlasSprite> spriteGetter,
                                        ResourceLocation modelName) {
        TextureAtlasSprite particle = spriteGetter.apply(modelConfiguration.resolveTexture("particle"));
        SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelConfiguration, overrides).setTexture(particle);
        for(BlockPart blockPart : blockParts) {
            for(Direction direction : blockPart.mapFaces.keySet()) {
                BlockPartFace blockPartFace = blockPart.mapFaces.get(direction);

                // Remove mandatory hash at start of texture name
                String texture = blockPartFace.texture;
                if (texture.charAt(0) == '#') {
                    texture = texture.substring(1);
                }
                TextureAtlasSprite sprite = spriteGetter.apply(modelConfiguration.resolveTexture(texture));

                if (blockPartFace.cullFace == null) {
                    builder.addGeneralQuad(BlockModel.makeBakedQuad(blockPart, blockPartFace, sprite, direction, transform, modelName));
                } else {
                    builder.addFaceQuad(Direction.rotateFace(transform.getRotation().getMatrix(), blockPartFace.cullFace),
                            BlockModel.makeBakedQuad(blockPart, blockPartFace, sprite, direction, transform, modelName));
                }
            }
        }
        return builder.build();
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        return TileHelpers.getSafeTile(world, pos, TileDisplayStand.class)
                .map(tile -> {
                    ModelDataMap.Builder builder = new ModelDataMap.Builder();
                    builder.withInitial(BlockDisplayStand.DIRECTION, tile.getDirection());
                    builder.withInitial(BlockDisplayStand.TYPE, tile.getDisplayStandType());
                    return (IModelData) builder.build();
                })
                .orElse(EmptyModelData.INSTANCE);
    }

    @Override
    public IBakedModel handleBlockState(BlockState state, Direction side, Random rand, IModelData modelData) {
        return handleDisplayStandType(ModelHelpers.getSafeProperty(modelData, BlockDisplayStand.TYPE, ItemStack.EMPTY), false);
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        return handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.getDisplayStandType(itemStack), true);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.untexturedBakedModel.getQuads(null, null, null)
                .stream()
                .map(quad -> new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), this.texture, quad.shouldApplyDiffuseLighting()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean func_230044_c_() {
        return true ; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return handleDisplayStandType(ModelHelpers.getSafeProperty(data, BlockDisplayStand.TYPE, ItemStack.EMPTY), false)
                .getParticleTexture();
    }
}
