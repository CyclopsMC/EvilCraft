package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A baked boec model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ModelBoxOfEternalClosureBaked extends DelegatingDynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ItemCameraTransforms TRANSFORMS = ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
            ItemCameraTransforms.TransformType.GUI, new ItemTransformVec3f(
                    new Vector3f(30, 135, 0),
                    new Vector3f(0, 0, 0),
                    new Vector3f(0.625f, 0.625f, 0.625f))
    ));

    public static IBakedModel boxModel;
    public static IBakedModel boxLidModel;
    public static IBakedModel boxLidRotatedModel;

    private final boolean isOpen;

    public ModelBoxOfEternalClosureBaked() {
        super();
        this.isOpen = false;
    }

    public ModelBoxOfEternalClosureBaked(boolean isOpen, ItemStack itemStack, World world, LivingEntity entity) {
        super(itemStack, world, entity);
        this.isOpen = isOpen;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        if (!isItemStack()) {
            return Collections.emptyList();
        }

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
    public IBakedModel handleBlockState(BlockState state, Direction side, Random rand, IModelData modelData) {
        return null;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        return new ModelBoxOfEternalClosureBaked(BlockBoxOfEternalClosure.getSpiritTypeWithFallbackSpirit(itemStack) == null,
                itemStack, world, entity);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return boxModel.getParticleIcon();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false; // TODO: rm
    }

    @Override
    public boolean usesBlockLight() {
        return false; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public boolean isCustomRenderer() {
        return false; // TODO: rm
    }

    @Override
    public ItemCameraTransforms getTransforms() {
        return TRANSFORMS;
    }
}
