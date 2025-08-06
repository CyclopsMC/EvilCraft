package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A baked broom part model.
 * @author rubensworks
 */
public class BroomPartModelBaked extends DynamicItemAndBlockModel {

    private final Map<IBroomPart, BlockStateModel> broomPartModels = Maps.newHashMap();
    private final RandomSource rand = RandomSource.create();

    public BroomPartModelBaked() {
        super(true, false);
    }

    public void addBroomPartModel(IBroomPart part, BlockStateModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos,
                                            BlockState state, Direction side,
                                            RandomSource rand, ModelData extraData,
                                            ChunkSectionLayer renderType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData tileData) {
        return null;
    }

    @Override
    public List<ChunkSectionLayer> getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return List.of(ChunkSectionLayer.values());
    }

    @Override
    public List<BakedQuad> handleItemState(@Nullable ItemStack itemStack, @Nullable Level world,
                                           @Nullable LivingEntity entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart part = BroomParts.REGISTRY.getPartFromItem(itemStack);
        BlockStateModel model = broomPartModels.get(part);
        if (model != null) {
            for (BlockModelPart blockModelPart : model.collectParts(this.rand)) {
                quads.addAll(color(blockModelPart.getQuads(null), part.getModelColor()));
            }
        }

        return quads;
    }

    /**
     * Color the quads
     * @param quads The original quads
     * @param color The color
     * @return The colored quads
     */
    private Collection<? extends BakedQuad> color(List<BakedQuad> quads, int color) {
        /*if (true) {
            return quads;
        }*/
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.vertices(), quad.vertices().length);
            for(int i = 0; i < vertexData.length / 8; i++) {
                vertexData[i * 8 + 3] = color;
            }
            offsetQuads.add(new BakedQuad(vertexData, quad.tintIndex(), quad.direction(), quad.sprite(), false, quad.lightEmission(), quad.hasAmbientOcclusion()));
        }
        return offsetQuads;
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return null;
    }

    @Override
    public UnbakedModel wrapped() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable ResolvedModel parent() {
        return null;
    }

    @Override
    public ItemTransforms getTopTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":broom_part";
    }
}
