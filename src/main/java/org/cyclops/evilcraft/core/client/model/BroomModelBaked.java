package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
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
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;

/**
 * A baked broom model.
 *
 * @author rubensworks
 */
public class BroomModelBaked extends DynamicItemAndBlockModel {

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

    private static final Map<IBroomPart, BlockStateModel> broomPartModels = Maps.newHashMap();
    private static TextureAtlasSprite particleIcon;

    private final List<BakedQuad> quads;
    private final RandomSource rand = RandomSource.create();

    public BroomModelBaked() {
        super(true, false);
        this.quads = Collections.emptyList();
    }

    public BroomModelBaked(List<BakedQuad> quads) {
        super(false, true);
        this.quads = Objects.requireNonNull(quads);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.quads;
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

    public static void addBroomModel(IBroomPart part, BlockStateModel bakedModel) {
        broomPartModels.put(part, bakedModel);
        if (part == BroomParts.ROD_WOOD) {
            particleIcon = bakedModel.particleIcon();
        }
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return particleIcon;
    }

    @Override
    public List<BakedQuad> handleItemState(@Nullable ItemStack itemStack, @Nullable Level world,
                                           @Nullable LivingEntity entity) {
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
                for (BlockModelPart blockModelPart : model.collectParts(this.rand)) {
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
            int[] vertexData = Arrays.copyOf(quad.vertices(), quad.vertices().length);
            for (int i = 0; i < vertexData.length / 8; i++) {
                float originalZ = Float.intBitsToFloat(vertexData[i * 8 + 2]);
                originalZ += offset;
                vertexData[i * 8 + 2] = Float.floatToIntBits(originalZ);
                vertexData[i * 8 + 3] = color;
            }

            offsetQuads.add(new BakedQuad(vertexData, quad.tintIndex(), quad.direction(), quad.sprite(), false, quad.lightEmission(), quad.hasAmbientOcclusion()));
        }

        return offsetQuads;
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

    public List<BakedQuad> getQuads() {
        return this.quads;
    }

    public RandomSource getRand() {
        return this.rand;
    }

    public String toString() {
        return "BroomModelBaked(quads=" + this.getQuads() + ", rand=" + this.getRand() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BroomModelBaked)) return false;
        final BroomModelBaked other = (BroomModelBaked) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$quads = this.getQuads();
        final Object other$quads = other.getQuads();
        if (this$quads == null ? other$quads != null : !this$quads.equals(other$quads)) return false;
        final Object this$rand = this.getRand();
        final Object other$rand = other.getRand();
        if (this$rand == null ? other$rand != null : !this$rand.equals(other$rand)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BroomModelBaked;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $quads = this.getQuads();
        result = result * PRIME + ($quads == null ? 43 : $quads.hashCode());
        final Object $rand = this.getRand();
        result = result * PRIME + ($rand == null ? 43 : $rand.hashCode());
        return result;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":broom";
    }
}
