package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.datastructure.SingleCache;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.core.client.model.ModelConfigurationRetextured;
import org.cyclops.evilcraft.blockentity.BlockEntityDisplayStand;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;

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
            return bakeModel(new ModelConfigurationRetextured(owner, textureName), blockModel.getElements(), transform,
                    ItemOverrides.EMPTY, ForgeModelBakery.instance().getSpriteMap()::getSprite, new ResourceLocation(Reference.MOD_ID, "dummy"));
        }

        @Override
        public boolean isKeyEqual(ResourceLocation resourceLocation, ResourceLocation k1) {
            return resourceLocation.equals(k1);
        }
    });

    private final BlockModel blockModel;
    private final BakedModel untexturedBakedModel;
    private final TextureAtlasSprite texture;
    private final IModelConfiguration owner;
    private final ModelState transform;

    public ModelDisplayStandBaked(BlockModel blockModel, BakedModel untexturedBakedModel, IModelConfiguration owner, ModelState transform) {
        super(true, false);
        this.blockModel = blockModel;
        this.untexturedBakedModel = untexturedBakedModel;
        this.owner = owner;
        this.transform = transform;
        this.texture = null;
    }

    public ModelDisplayStandBaked(BlockModel blockModel, BakedModel untexturedBakedModel, TextureAtlasSprite texture, boolean item, IModelConfiguration owner, ModelState transform) {
        super(false, item);
        this.blockModel = blockModel;
        this.untexturedBakedModel = untexturedBakedModel;
        this.texture = texture;
        this.owner = owner;
        this.transform = transform;
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
                    .getBlockModel(blockState).getParticleIcon().getName();
            return modelCache.get(textureName);
        }
        return untexturedBakedModel;
    }

    // Inspired by TCon's TableModel, SimpleBlockModel and RetexturedModel
    public static BakedModel bakeModel(IModelConfiguration modelConfiguration, List<BlockElement> blockParts, ModelState transform,
                                        ItemOverrides overrides, Function<Material, TextureAtlasSprite> spriteGetter,
                                        ResourceLocation modelName) {
        TextureAtlasSprite particle = spriteGetter.apply(modelConfiguration.resolveTexture("particle"));
        SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelConfiguration, overrides).particle(particle);
        for(BlockElement blockPart : blockParts) {
            for(Direction direction : blockPart.faces.keySet()) {
                BlockElementFace blockPartFace = blockPart.faces.get(direction);

                // Remove mandatory hash at start of texture name
                String texture = blockPartFace.texture;
                if (texture.charAt(0) == '#') {
                    texture = texture.substring(1);
                }
                TextureAtlasSprite sprite = spriteGetter.apply(modelConfiguration.resolveTexture(texture));

                if (blockPartFace.cullForDirection == null) {
                    builder.addUnculledFace(BlockModel.makeBakedQuad(blockPart, blockPartFace, sprite, direction, transform, modelName));
                } else {
                    builder.addCulledFace(Direction.rotate(transform.getRotation().getMatrix(), blockPartFace.cullForDirection),
                            BlockModel.makeBakedQuad(blockPart, blockPartFace, sprite, direction, transform, modelName));
                }
            }
        }
        return builder.build();
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        return BlockEntityHelpers.get(world, pos, BlockEntityDisplayStand.class)
                .map(tile -> {
                    ModelDataMap.Builder builder = new ModelDataMap.Builder();
                    builder.withInitial(BlockDisplayStand.DIRECTION, tile.getDirection());
                    builder.withInitial(BlockDisplayStand.TYPE, tile.getDisplayStandType());
                    return (IModelData) builder.build();
                })
                .orElse(EmptyModelData.INSTANCE);
    }

    @Override
    public BakedModel handleBlockState(BlockState state, Direction side, Random rand, IModelData modelData) {
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
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        return handleDisplayStandType(ModelHelpers.getSafeProperty(data, BlockDisplayStand.TYPE, ItemStack.EMPTY), false)
                .getParticleIcon();
    }
}
