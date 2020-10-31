package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
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
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * A baked broom model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BroomModelBaked extends DynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ItemCameraTransforms PERSPECTIVE_TRANSFORMS =
            ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
                    ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    new ItemTransformVec3f(
                            new Vector3f(0, 0, 0),
                            new Vector3f(90, 180, 90),
                            new Vector3f(1, 1, 1)
                    ),
                    ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                    new ItemTransformVec3f(
                            new Vector3f(0, 0, 0),
                            new Vector3f(90, 180, 90),
                            new Vector3f(1, 1, 1)
                    ),
                    ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                    new ItemTransformVec3f(
                            new Vector3f(0.25F, -0.025F, 0),
                            new Vector3f(10, 190, 100),
                            new Vector3f(1, 1, 1)
                    ),
                    ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                    new ItemTransformVec3f(
                            new Vector3f(0.25F, -0.025F, 0),
                            new Vector3f(10, 190, 100),
                            new Vector3f(1, 1, 1)
                    )
            ));

    private static final Map<IBroomPart, IBakedModel> broomPartModels = Maps.newHashMap();

    private final List<BakedQuad> quads;
    private final Random rand = new Random();

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
    public IBakedModel handleBlockState(@Nullable BlockState blockState, @Nullable Direction direction, @Nonnull Random random, @Nonnull IModelData iModelData) {
        throw new UnsupportedOperationException();
    }

    public static void addBroomModel(IBroomPart part, IBakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @Override
    public boolean func_230044_c_() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart rod = null;
        Collection<IBroomPart> parts = BroomParts.REGISTRY.getBroomParts(itemStack);
        for (IBroomPart part : parts) {
            if (part.getType() == IBroomPart.BroomPartType.ROD && rod == null) {
                rod = part;
            } else {
                // TODO: invalid broom
            }
        }

        if (rod == null) {
            // TODO: invalid broom
        }

        AtomicLongMap<IBroomPart.BroomPartType> partTypeOccurences = AtomicLongMap.create();
        for (IBroomPart part : parts) {
            IBakedModel model = broomPartModels.get(part);
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
     * @param quads The original quads
     * @param offset The offset to apply
     * @param color The color
     * @return The offsetted quads
     */
    private Collection<? extends BakedQuad> offsetAndColor(List<BakedQuad> quads, float offset, int color) {
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length);
            for(int i = 0; i < vertexData.length / 7; i++) {
                float originalZ = Float.intBitsToFloat(vertexData[i * 7 + 2]);
                originalZ += offset;
                vertexData[i * 7 + 2] = Float.floatToIntBits(originalZ);
                vertexData[i * 7 + 3] = color;
            }

            offsetQuads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.func_187508_a(), false));
        }

        return offsetQuads;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return PERSPECTIVE_TRANSFORMS;
    }
}
