package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.quad.BakedColors;
import net.neoforged.neoforge.client.model.quad.BakedNormals;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A baked broom part model.
 * @author rubensworks
 */
public class ModelBroomPartBaked extends DynamicItemAndBlockModel {

    private final Map<IBroomPart, BlockStateModel> broomPartModels;
    private final RandomSource rand = RandomSource.create();

    public ModelBroomPartBaked(Map<IBroomPart, BlockStateModel> broomPartModels) {
        super(true, false);
        this.broomPartModels = broomPartModels;
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
                                           @Nullable ItemOwner entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart part = BroomParts.REGISTRY.getPartFromItem(itemStack);
        BlockStateModel model = broomPartModels.get(part);
        if (model != null) {
            List<BlockStateModelPart> partsList = new ArrayList<>();
            model.collectParts(this.rand, partsList);
            for (BlockStateModelPart blockModelPart : partsList) {
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
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            offsetQuads.add(new BakedQuad(quad.position0(), quad.position1(), quad.position2(), quad.position3(), quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(), quad.direction(), quad.materialInfo(), BakedNormals.UNSPECIFIED, BakedColors.of(color)));
        }
        return offsetQuads;
    }

    @Override
    public Material.Baked particleMaterial() {
        return null;
    }

    @Override
    public int materialFlags() {
        return 0;
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
