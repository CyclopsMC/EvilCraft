package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.DynamicBaseModel;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.List;

/**
 * A baked boec model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ModelBoxOfEternalClosureBaked extends DynamicBaseModel implements ISmartItemModel {

    // Gui person transform for block items
    private static final TRSRTransformation GUI = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 180, 0)),
            new Vector3f(1, 1, 1),
            null));

    // Default perspective transforms
    protected static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> PERSPECTIVE_TRANSFORMS =
            ImmutableMap.of(
                    ItemCameraTransforms.TransformType.THIRD_PERSON, DynamicBaseModel.THIRD_PERSON,
                    ItemCameraTransforms.TransformType.GUI, GUI);

    public static IBakedModel boxModel;
    public static IBakedModel boxLidModel;
    public static IBakedModel boxLidRotatedModel;

    private final boolean isOpen;

    public ModelBoxOfEternalClosureBaked() {
        this.isOpen = false;
    }

    public ModelBoxOfEternalClosureBaked(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads = Lists.newLinkedList();

        quads.addAll(boxModel.getGeneralQuads());
        if(isOpen) {
            quads.addAll(boxLidRotatedModel.getGeneralQuads());
        } else {
            quads.addAll(boxLidModel.getGeneralQuads());
        }

        return quads;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        return new ModelBoxOfEternalClosureBaked(BoxOfEternalClosure.getInstance().getSpiritName(itemStack) == null);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return boxModel.getParticleTexture();
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, PERSPECTIVE_TRANSFORMS, cameraTransformType);
    }
}
