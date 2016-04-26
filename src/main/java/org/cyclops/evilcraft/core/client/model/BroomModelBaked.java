package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.DynamicBaseModel;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A baked broom model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BroomModelBaked extends DynamicBaseModel implements ISmartItemModel {

    private static final TRSRTransformation THIRD_PERSON = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, 180, 170)),
            new Vector3f(1, 1, 1),
            null));
    private static final TRSRTransformation FIRST_PERSON = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0.25F, -0.025F, 0),
            TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, 135, 10)),
            new Vector3f(2, 2, 2),
            null));

    // Default perspective transforms
    private static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> PERSPECTIVE_TRANSFORMS =
            ImmutableMap.of(
                    ItemCameraTransforms.TransformType.THIRD_PERSON, THIRD_PERSON,
                    ItemCameraTransforms.TransformType.FIRST_PERSON, FIRST_PERSON);

    private static final Map<IBroomPart, IBakedModel> broomPartModels = Maps.newHashMap();

    private final List<BakedQuad> quads;

    public BroomModelBaked() {
        this.quads = null;
    }

    public BroomModelBaked(List<BakedQuad> quads) {
        this.quads = quads;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.quads;
    }

    public static void addBroomModel(IBroomPart part, IBakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart rod = null;
        Collection<IBroomPart> parts = BroomParts.REGISTRY.getBroomParts(itemStack);
        for (IBroomPart part : parts) {
            if (part.getType() == IBroomPart.BroomPartType.ROD && rod == null) {
                rod = part;
            } else {
                // TODO: invalid broom
            }
        }

        if (rod == null) {
            // TODO: invalid broom
        }

        AtomicLongMap<IBroomPart.BroomPartType> partTypeOccurences = AtomicLongMap.create();
        for (IBroomPart part : parts) {
            IBakedModel model = broomPartModels.get(part);
            if (model != null) {
                List<BakedQuad> originalQuads = model.getGeneralQuads();
                int typeIndex = (int) partTypeOccurences.getAndIncrement(part.getType());
                float offset = part.getType().getOffsetter().getOffset(rod.getLength(), part.getLength(), typeIndex);
                int color = part.getModelColor();
                quads.addAll(offsetAndColor(originalQuads, offset, color));
            }
        }

        return new BroomModelBaked(quads);
    }

    /**
     * Offsets the z coordinate and color the quads
     * @param quads The original quads
     * @param offset The offset to apply
     * @param color The color
     * @return The offsetted quads
     */
    private Collection<? extends BakedQuad> offsetAndColor(List<BakedQuad> quads, float offset, int color) {
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length);
            for(int i = 0; i < vertexData.length / 7; i++) {
                float originalZ = Float.intBitsToFloat(vertexData[i * 7 + 2]);
                originalZ += offset;
                vertexData[i * 7 + 2] = Float.floatToIntBits(originalZ);
                vertexData[i * 7 + 3] = color;
            }

            offsetQuads.add(new IColoredBakedQuad.ColoredBakedQuad(vertexData, quad.getTintIndex(), quad.getFace()));
        }

        return offsetQuads;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, PERSPECTIVE_TRANSFORMS, cameraTransformType);
    }
}
