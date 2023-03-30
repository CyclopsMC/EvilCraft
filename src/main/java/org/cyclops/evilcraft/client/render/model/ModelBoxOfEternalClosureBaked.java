package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

/**
 * A baked boec model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ModelBoxOfEternalClosureBaked extends DelegatingDynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ItemTransforms TRANSFORMS = ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
            ItemDisplayContext.GUI, new ItemTransform(
                    new Vector3f(30, 135, 0),
                    new Vector3f(0, 0, 0),
                    new Vector3f(0.625f, 0.625f, 0.625f))
    ));

    public static BakedModel boxModel;
    public static BakedModel boxLidModel;
    public static BakedModel boxLidRotatedModel;

    private final boolean isOpen;

    public ModelBoxOfEternalClosureBaked() {
        super();
        this.isOpen = false;
    }

    public ModelBoxOfEternalClosureBaked(boolean isOpen, ItemStack itemStack, Level world, LivingEntity entity) {
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
    public BakedModel handleBlockState(BlockState state, Direction side, RandomSource rand, ModelData modelData, RenderType renderType) {
        return null;
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        return new ModelBoxOfEternalClosureBaked(BlockBoxOfEternalClosure.getSpiritTypeWithFallbackSpirit(itemStack) == null,
                itemStack, world, entity);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return boxModel.getParticleIcon();
    }

    @Override
    public boolean usesBlockLight() {
        return false; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public ItemTransforms getTransforms() {
        return TRANSFORMS;
    }
}
