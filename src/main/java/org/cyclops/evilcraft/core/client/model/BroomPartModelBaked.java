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
import net.minecraftforge.client.model.IColoredBakedQuad;
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
 * A baked broom part model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BroomPartModelBaked extends DynamicBaseModel implements ISmartItemModel {

    private final Map<IBroomPart, IBakedModel> broomPartModels = Maps.newHashMap();

    public BroomPartModelBaked() {
    }

    public void addBroomPartModel(IBroomPart part, IBakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart part = BroomParts.REGISTRY.getPartFromItem(itemStack);
        IBakedModel model = broomPartModels.get(part);
        if (model != null) {
            quads.addAll(color(model.getGeneralQuads(), part.getModelColor()));
        }

        return new SimpleBakedModel(quads, ModelHelpers.EMPTY_FACE_QUADS, this.isAmbientOcclusion(), this.isGui3d(),
                this.getParticleTexture(), this.getItemCameraTransforms());
    }

    /**
     * Color the quads
     * @param quads The original quads
     * @param color The color
     * @return The colored quads
     */
    private Collection<? extends BakedQuad> color(List<BakedQuad> quads, int color) {
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length);
            for(int i = 0; i < vertexData.length / 7; i++) {
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
}
