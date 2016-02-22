package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
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

import java.util.List;

/**
 * A baked boec model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ModelBoxOfEternalClosureBaked extends DynamicBaseModel implements ISmartItemModel {

    public static IBakedModel boxModel;
    public static IBakedModel boxLidModel;

    public ModelBoxOfEternalClosureBaked() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        List<BakedQuad> quads = Lists.newLinkedList();

        quads.addAll(boxModel.getGeneralQuads());
        quads.addAll(boxLidModel.getGeneralQuads());

        // TODO: rotate lid based on item state (empty or non-empty)

        return new SimpleBakedModel(quads, ModelHelpers.EMPTY_FACE_QUADS, this.isAmbientOcclusion(), this.isGui3d(),
                this.getParticleTexture(), this.getItemCameraTransforms());
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return boxModel.getParticleTexture();
    }
}
