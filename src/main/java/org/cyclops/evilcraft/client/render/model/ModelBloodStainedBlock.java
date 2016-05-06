package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.cyclops.cyclopscore.client.model.DelegatingChildDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.block.BloodStainedBlock;

import java.util.List;

/**
 * Dynamic model for blood stained blocks.
 * @author rubensworks
 */
public class ModelBloodStainedBlock extends DelegatingChildDynamicItemAndBlockModel {

    private final TextureAtlasSprite overlayIcon;
    private final IBlockState innerBlockState;

    public ModelBloodStainedBlock() {
        super(null);
        this.overlayIcon = null;
        this.innerBlockState = null;
    }

    public ModelBloodStainedBlock(IBakedModel baseModel, TextureAtlasSprite overlayIcon, boolean item,
                                  IBlockState blockState, EnumFacing facing, long rand, IBlockState innerBlockState) {
        super(baseModel, blockState, facing, rand);
        this.overlayIcon = overlayIcon;
        this.innerBlockState = innerBlockState;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads;
        try {
            quads = Lists.newArrayList(baseModel.getQuads(innerBlockState, getRenderingSide(), rand));
        } catch (Exception e) {
            quads = Lists.newArrayList(); // It's better to render a bit stranger, than to crash all together.
        }
        if(facing == EnumFacing.UP || facing == null) {
            addBakedQuad(quads, 0, 1, 0, 1, 1.01F, overlayIcon, EnumFacing.UP);
        }
        return quads;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state, EnumFacing side, long rand) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        IBlockState blockState = BlockHelpers.getSafeBlockStateProperty(extendedBlockState, BloodStainedBlock.INNERBLOCK, null);
        BlockPos pos = BlockHelpers.getSafeBlockStateProperty(extendedBlockState, BloodStainedBlock.POS, null);
        TextureAtlasSprite overlayIcon = getIcon(pos);
        IBakedModel baseModel = RenderHelpers.getBakedModel(blockState);
        return new ModelBloodStainedBlock(baseModel, overlayIcon, false, state, facing, rand, blockState);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack, World world, EntityLivingBase entity) {
        return null;
    }

    protected TextureAtlasSprite getIcon(BlockPos pos) {
        TextureAtlasSprite[] icons = new TextureAtlasSprite[3];
        icons[0] = BloodStainedBlock.getInstance().icon0;
        icons[1] = BloodStainedBlock.getInstance().icon1;
        icons[2] = BloodStainedBlock.getInstance().icon2;
        return icons[pos == null ? 0 : Math.abs(pos.hashCode()) % 3];
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return RenderHelpers.getBakedModel(Blocks.stone.getDefaultState()).getParticleTexture();
    }
}
