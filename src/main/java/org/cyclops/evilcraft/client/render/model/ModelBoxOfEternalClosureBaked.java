package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
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
public class ModelBoxOfEternalClosureBaked extends DelegatingDynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> PERSPECTIVE_TRANSFORMS =
            ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(ItemCameraTransforms.TransformType.GUI,
                    TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                            new Vector3f(0, 0, 0),
                            TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 45, 0)),
                            new Vector3f(0.625f, 0.625f, 0.625f),
                            null))));

    public static IBakedModel boxModel;
    public static IBakedModel boxLidModel;
    public static IBakedModel boxLidRotatedModel;

    private final boolean isOpen;

    public ModelBoxOfEternalClosureBaked() {
        super();
        this.isOpen = false;
    }

    public ModelBoxOfEternalClosureBaked(IBlockState blockState, EnumFacing facing, long rand) {
        super(blockState, facing, rand);
        this.isOpen = false;
    }

    public ModelBoxOfEternalClosureBaked(boolean isOpen, ItemStack itemStack, World world, EntityLivingBase entity) {
        super(itemStack, world, entity);
        this.isOpen = isOpen;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads = Lists.newLinkedList();

        quads.addAll(boxModel.getQuads(blockState, facing, rand));
        if(isOpen) {
            quads.addAll(boxLidRotatedModel.getQuads(blockState, facing, rand));
        } else {
            quads.addAll(boxLidModel.getQuads(blockState, facing, rand));
        }

        return quads;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state, EnumFacing side, long rand) {
        return null;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, EntityLivingBase entity) {
        return new ModelBoxOfEternalClosureBaked(BoxOfEternalClosure.getInstance().getSpiritName(itemStack) == null,
                itemStack, world, entity);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return boxModel.getParticleTexture();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, PERSPECTIVE_TRANSFORMS, cameraTransformType);
    }
}
