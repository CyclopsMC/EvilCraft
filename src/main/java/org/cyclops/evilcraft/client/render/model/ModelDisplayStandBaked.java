package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDisplayStand;
import org.cyclops.evilcraft.tileentity.TileDisplayStand;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Random;

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
    private static final Map<String, IBakedModel> retexturedCache = Maps.newHashMap();

    private final IBakedModel untexturedBakedModel;
    private final IUnbakedModel retexturableModel;

    public ModelDisplayStandBaked(IBakedModel untexturedBakedModel, IUnbakedModel retexturableModel) {
        super(true, false);
        this.untexturedBakedModel = untexturedBakedModel;
        this.retexturableModel = retexturableModel;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return untexturedBakedModel.getParticleTexture();
    }

    protected IBakedModel handleDisplayStandType(ItemStack displayStandType, boolean axisX, Direction facing) {
        if (displayStandType != null && !displayStandType.isEmpty()) {
            // Get reference texture
            BlockState blockState = BlockHelpers.getBlockStateFromItemStack(displayStandType);
            ResourceLocation textureName = Minecraft.getInstance().getBlockRendererDispatcher()
                    .getBlockModelShapes().getTexture(blockState).getName();

            // Retexture our model
            String cacheName = textureName.toString() + axisX + facing.toString();
            IBakedModel bakedRetexturedModel = retexturedCache.get(cacheName);
            if (bakedRetexturedModel == null) {
                /*IModel retexturedModel = retexturableModel.retexture(ImmutableMap.of("base", textureName));
                ModelRotation modelState = ROTATIONS.get(facing); // Hack, see DisplayStand#bakeVariantModel, should just be retexturableModel.getDefaultState();
                bakedRetexturedModel = retexturedModel.bake(modelState, DefaultVertexFormats.BLOCK, org.cyclops.cyclopscore.helper.RenderHelpers.TEXTURE_GETTER);
                TODO
                 */
                retexturedCache.put(cacheName, bakedRetexturedModel);
            }

            return bakedRetexturedModel;
        }
        return untexturedBakedModel;
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
        return handleDisplayStandType(ModelHelpers.getSafeProperty(modelData, BlockDisplayStand.TYPE, ItemStack.EMPTY),
                state.get(BlockDisplayStand.AXIS_X), state.get(BlockDisplayStand.FACING));
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        return handleDisplayStandType(RegistryEntries.BLOCK_DISPLAY_STAND.getDisplayStandType(itemStack),
                true, Direction.DOWN);
    }

    @Override
    public boolean func_230044_c_() {
        return false; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }
}
