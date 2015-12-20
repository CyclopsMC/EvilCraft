package org.cyclops.evilcraft.client.render.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import org.cyclops.cyclopscore.client.model.DynamicModel;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;

import java.util.Collections;
import java.util.List;

/**
 * Dynamic model for {@link ConfigurableBlockWithInnerBlocks}.
 * @author rubensworks
 */
public class ModelInnerBlock extends DynamicModel {

    private final ConfigurableBlockWithInnerBlocks block;
    private final IBakedModel baseModel;

    public ModelInnerBlock(ConfigurableBlockWithInnerBlocks block) {
        this.block = block;
        this.baseModel = null;
    }

    public ModelInnerBlock(ConfigurableBlockWithInnerBlocks block, IBakedModel baseModel) {
        this.block = block;
        this.baseModel = baseModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        if(baseModel == null) return Collections.emptyList();
        return baseModel.getFaceQuads(side);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        if(baseModel == null) return Collections.emptyList();
        return baseModel.getGeneralQuads();
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IBlockState innerBlockState = block.getBlockFromState(state);
        IBakedModel baseModel = RenderHelpers.getBakedModel(innerBlockState);
        if(baseModel instanceof ISmartBlockModel) {
            baseModel = ((ISmartBlockModel) baseModel).handleBlockState(innerBlockState);
        }
        return new ModelInnerBlock(block, baseModel);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        IBlockState innerBlockState = block.getBlockFromMeta(stack.getItemDamage());
        return RenderHelpers.getBakedModel(innerBlockState);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return RenderHelpers.getBakedModel(block.getBlockFromMeta(0)).getParticleTexture();
    }
}
