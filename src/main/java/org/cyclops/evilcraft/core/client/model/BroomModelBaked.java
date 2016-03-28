package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ISmartItemModel;
import org.cyclops.cyclopscore.client.model.DynamicBaseModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

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

    private final Map<IBroomPart, IBakedModel> broomPartModels = Maps.newHashMap();

    public BroomModelBaked() {
    }

    public void addBroomModel(IBroomPart part, IBakedModel bakedModel) {
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

        for (IBroomPart part : parts) {
            float offset = part.getType().getOffsetter().getOffset(rod.getLength(), part.getLength());
            quads.addAll(offset(broomPartModels.get(part).getGeneralQuads(), offset));
        }

        return new SimpleBakedModel(quads, ModelHelpers.EMPTY_FACE_QUADS, this.isAmbientOcclusion(), this.isGui3d(),
                this.getParticleTexture(), this.getItemCameraTransforms());
    }

    /**
     * Offsets the z coordinate
     * @param quads The original quads
     * @param offset The offset to apply
     * @return The offsetted quads
     */
    private Collection<? extends BakedQuad> offset(List<BakedQuad> quads, float offset) {
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length);
            for(int i = 0; i < vertexData.length / 7; i++) {
                float originalZ = Float.intBitsToFloat(vertexData[i * 7 + 2]);
                originalZ += offset;
                vertexData[i * 7 + 2] = Float.floatToIntBits(originalZ);
            }

            offsetQuads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace()));
        }

        return offsetQuads;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }
}
