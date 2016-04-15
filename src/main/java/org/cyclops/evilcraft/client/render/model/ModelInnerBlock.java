package org.cyclops.evilcraft.client.render.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;

import java.util.Collections;
import java.util.List;

/**
 * Dynamic model for {@link ConfigurableBlockWithInnerBlocks}.
 * @author rubensworks
 */
public class ModelInnerBlock extends DynamicItemAndBlockModel {

    private final ConfigurableBlockWithInnerBlocks block;
    private final IBakedModel baseModel;
    private final IBlockState blockState;
    private final EnumFacing facing;
    private final long rand;

    public ModelInnerBlock(ConfigurableBlockWithInnerBlocks block) {
        super(true, false);
        this.block = block;
        this.baseModel = null;
        this.blockState = null;
        this.facing = null;
        this.rand = 0;
    }

    public ModelInnerBlock(ConfigurableBlockWithInnerBlocks block, IBakedModel baseModel, boolean item,
                           IBlockState blockState, EnumFacing facing, long rand) {
        super(false, item);
        this.block = block;
        this.baseModel = baseModel;
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        if(baseModel == null) return Collections.emptyList();
        return baseModel.getQuads(blockState, facing, rand);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState blockState, EnumFacing side, long rand) {
        IBlockState innerBlockState = block.getBlockFromState(blockState);
        IBakedModel baseModel = RenderHelpers.getBakedModel(innerBlockState);
        return new ModelInnerBlock(block, baseModel, false, innerBlockState, side, rand);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack, World world, EntityLivingBase entity) {
        IBlockState innerBlockState = block.getBlockFromMeta(stack != null ? stack.getItemDamage() : 0);
        return RenderHelpers.getBakedModel(innerBlockState);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return RenderHelpers.getBakedModel(block.getBlockFromMeta(0)).getParticleTexture();
    }
}
