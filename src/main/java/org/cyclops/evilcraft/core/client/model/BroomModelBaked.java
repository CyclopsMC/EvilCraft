package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;

import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Map;

/**
 * A baked broom model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BroomModelBaked extends DynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> PERSPECTIVE_TRANSFORMS =
            ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
                    ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                            new Vector3f(0, 0, 0),
                            TRSRTransformation.quatFromXYZDegrees(new Vector3f(90, 180, 90)),
                            new Vector3f(1, 1, 1),
                            null)),
                    ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                    TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                            new Vector3f(0, 0, 0),
                            TRSRTransformation.quatFromXYZDegrees(new Vector3f(90, 180, 90)),
                            new Vector3f(1, 1, 1),
                            null)),
                    ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                    TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                            new Vector3f(0.25F, -0.025F, 0),
                            TRSRTransformation.quatFromXYZDegrees(new Vector3f(10, 190, 100)),
                            new Vector3f(1, 1, 1),
                            null)),
                    ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                    TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                            new Vector3f(0.25F, -0.025F, 0),
                            TRSRTransformation.quatFromXYZDegrees(new Vector3f(10, 190, 100)),
                            new Vector3f(1, 1, 1),
                            null))
            ));

    private final Map<IBroomPart, IBakedModel> broomPartModels = Maps.newHashMap();

    public BroomModelBaked() {
        super(true, true);
    }

    public void addBroomModel(IBroomPart part, IBakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state, EnumFacing side, long rand) {
        return null; // Not required
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack, World world, EntityLivingBase entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        // TODO: check the broom parts and render accordingly
        // TODO: For now we just render all parts
        for(IBakedModel model : broomPartModels.values()) {
            quads.addAll(model.getQuads(null, null, 0));
        }

        return new MapWrapper(new SimpleBakedModel(quads, ModelHelpers.EMPTY_FACE_QUADS, this.isAmbientOcclusion(), this.isGui3d(),
                this.getParticleTexture(), this.getItemCameraTransforms(), ItemOverrideList.NONE), PERSPECTIVE_TRANSFORMS);
    }
}
