package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;
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
import net.neoforged.neoforge.client.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
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

    private static final Map<IBroomPart, BakedModel> broomPartModels = Maps.newHashMap();

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
    public BakedModel handleBlockState(@Nullable BlockState blockState, @Nullable Direction direction, @Nonnull RandomSource random, @Nonnull ModelData iModelData, @Nullable RenderType renderType) {
        throw new UnsupportedOperationException();
    }

    public static void addBroomModel(IBroomPart part, BakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
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
            BakedModel model = broomPartModels.get(part);
            if (model != null) {
                List<BakedQuad> originalQuads = model.getQuads(null, null, this.rand);
                int typeIndex = (int) partTypeOccurences.getAndIncrement(part.getType());
                float offset = part.getType().getOffsetter().getOffset(rod.getLength(), part.getLength(), typeIndex);
                int color = part.getModelColor();
                quads.addAll(offsetAndColor(originalQuads, offset, color));
            }
        }

        return new BroomModelBaked(quads);
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
            int[] vertexData = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
            for (int i = 0; i < vertexData.length / 8; i++) {
                float originalZ = Float.intBitsToFloat(vertexData[i * 8 + 2]);
                originalZ += offset;
                vertexData[i * 8 + 2] = Float.floatToIntBits(originalZ);
                vertexData[i * 8 + 3] = color;
            }

            offsetQuads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), false, quad.getLightEmission(), quad.hasAmbientOcclusion()));
        }

        return offsetQuads;
    }

    @Override
    public ItemTransforms getTransforms() {
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
}
