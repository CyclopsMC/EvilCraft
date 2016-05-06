package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.cyclops.cyclopscore.client.model.DynamicBaseModel;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.block.DisplayStand;

import java.util.Map;

/**
 * The dynamic item model for the display stand.
 * Inspired by TCon's dynamic tool table retexturing.
 * @author rubensworks
 */
public class ModelDisplayStand extends DynamicBaseModel implements ISmartBlockModel, ISmartItemModel {

    private static final Map<EnumFacing, ModelRotation> ROTATIONS = ImmutableMap.<EnumFacing, ModelRotation>builder()
            .put(EnumFacing.NORTH, ModelRotation.X270_Y0)
            .put(EnumFacing.SOUTH, ModelRotation.X90_Y0)
            .put(EnumFacing.WEST, ModelRotation.X90_Y90)
            .put(EnumFacing.EAST, ModelRotation.X270_Y90)
            .put(EnumFacing.UP, ModelRotation.X180_Y0)
            .put(EnumFacing.DOWN, ModelRotation.X0_Y0)
            .build();
    private static final Map<String, IBakedModel> retexturedCache = Maps.newHashMap();

    private final IPerspectiveAwareModel untexturedBakedModel;
    private final IRetexturableModel retexturableModel;

    public ModelDisplayStand(IPerspectiveAwareModel untexturedBakedModel, IRetexturableModel retexturableModel) {
        this.untexturedBakedModel = untexturedBakedModel;
        this.retexturableModel = retexturableModel;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return untexturedBakedModel.getParticleTexture();
    }

    protected IBakedModel handleDisplayStandType(ItemStack displayStandType, boolean axisX, EnumFacing facing) {
        if (displayStandType != null) {
            // Get reference texture
            IBlockState blockState = BlockHelpers.getBlockStateFromItemStack(displayStandType);
            String textureName = Minecraft.getMinecraft().getBlockRendererDispatcher()
                    .getBlockModelShapes().getTexture(blockState).getIconName();

            // Retexture our model
            String cacheName = textureName + axisX + facing.toString();
            IBakedModel bakedRetexturedModel = retexturedCache.get(cacheName);
            if (bakedRetexturedModel == null) {
                IModel retexturedModel = retexturableModel.retexture(ImmutableMap.of("base", textureName));
                IModelState modelState = ROTATIONS.get(facing); // Hack, see DisplayStand#bakeVariantModel, should just be retexturableModel.getDefaultState();
                bakedRetexturedModel = retexturedModel.bake(modelState, DefaultVertexFormats.BLOCK, org.cyclops.cyclopscore.helper.RenderHelpers.TEXTURE_GETTER);
                retexturedCache.put(cacheName, bakedRetexturedModel);
            }

            return bakedRetexturedModel;
        }
        return untexturedBakedModel;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        return handleDisplayStandType(DisplayStand.getInstance().getDisplayStandType(itemStack), true, EnumFacing.DOWN);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        return handleDisplayStandType(((IExtendedBlockState) state).getValue(DisplayStand.TYPE),
                state.getValue(DisplayStand.AXIS_X), state.getValue(DisplayStand.FACING));
    }
}
