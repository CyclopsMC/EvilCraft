package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicLongMap;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
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
import org.joml.Vector3f;
import org.joml.Vector3fc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A baked broom model.
 *
 * @author rubensworks
 */
public class ModelBroomBaked extends DynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ItemTransforms PERSPECTIVE_TRANSFORMS =
            ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
                    ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                    new ItemTransform(
                            new Vector3f(90, 180, 90),
                            new Vector3f(0, 0, 0),
                            new Vector3f(1, 1, 1)
                    ),
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                    new ItemTransform(
                            new Vector3f(90, 180, 90),
                            new Vector3f(0, 0, 0),
                            new Vector3f(1, 1, 1)
                    ),
                    ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                    new ItemTransform(
                            new Vector3f(10, 190, 100),
                            new Vector3f(0.25F, -0.025F, 0),
                            new Vector3f(1, 1, 1)
                    ),
                    ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                    new ItemTransform(
                            new Vector3f(10, 190, 100),
                            new Vector3f(0.25F, -0.025F, 0),
                            new Vector3f(1, 1, 1)
                    )
            ));

    private final Map<IBroomPart, BlockStateModel> broomPartModels;

    private final RandomSource rand = RandomSource.create();

    public ModelBroomBaked(Map<IBroomPart, BlockStateModel> broomPartModels) {
        super(true, false);
        this.broomPartModels = broomPartModels;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
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
        return List.of();
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
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
    public List<BakedQuad> handleItemState(@Nullable ItemStack itemStack, @Nullable Level world,
                                           @Nullable ItemOwner entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart rod = null;
        Collection<IBroomPart> parts = BroomParts.REGISTRY.getBroomParts(itemStack);
        for (IBroomPart part : parts) {
            if (part.getType() == IBroomPart.BroomPartType.ROD && rod == null) {
                rod = part;
            }
        }

        AtomicLongMap<IBroomPart.BroomPartType> partTypeOccurences = AtomicLongMap.create();
        for (IBroomPart part : parts) {
            BlockStateModel model = broomPartModels.get(part);
            if (model != null) {
                List<BlockStateModelPart> partsList = new ArrayList<>();
                model.collectParts(this.rand, partsList);
                for (BlockStateModelPart blockModelPart : partsList) {
                    List<BakedQuad> originalQuads = blockModelPart.getQuads(null);
                    int typeIndex = (int) partTypeOccurences.getAndIncrement(part.getType());
                    float offset = part.getType().getOffsetter().getOffset(rod.getLength(), part.getLength(), typeIndex);
                    int color = part.getModelColor();
                    quads.addAll(offsetAndColor(originalQuads, offset, color));
                }
            }
        }

        return quads;
    }

    /**
     * Offsets the z coordinate and color the quads
     *
     * @param quads  The original quads
     * @param offset The offset to apply
     * @param color  The color
     * @return The offsetted quads
     */
    private Collection<? extends BakedQuad> offsetAndColor(List<BakedQuad> quads, float offset, int color) {
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            offsetQuads.add(new BakedQuad(offsetVec(quad.position0(), offset), offsetVec(quad.position1(), offset), offsetVec(quad.position2(), offset), offsetVec(quad.position3(), offset), quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(), quad.direction(), quad.materialInfo(), BakedNormals.UNSPECIFIED, BakedColors.of(color)));
        }

        return offsetQuads;
    }

    private Vector3fc offsetVec(Vector3fc vector3fc, float offset) {
        return new Vector3f(vector3fc.x(), vector3fc.y(), vector3fc.z()).add(0, 0, offset);
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
        return PERSPECTIVE_TRANSFORMS;
    }

    public RandomSource getRand() {
        return this.rand;
    }

    public String toString() {
        return "BroomModelBaked(rand=" + this.getRand() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ModelBroomBaked)) return false;
        final ModelBroomBaked other = (ModelBroomBaked) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$rand = this.getRand();
        final Object other$rand = other.getRand();
        if (this$rand == null ? other$rand != null : !this$rand.equals(other$rand)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ModelBroomBaked;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $rand = this.getRand();
        result = result * PRIME + ($rand == null ? 43 : $rand.hashCode());
        return result;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":broom";
    }
}
