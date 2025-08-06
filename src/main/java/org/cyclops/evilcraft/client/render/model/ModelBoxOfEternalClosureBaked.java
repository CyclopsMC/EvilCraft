package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * A baked boec model.
 *
 * @author rubensworks
 */
public class ModelBoxOfEternalClosureBaked extends DelegatingDynamicItemAndBlockModel {

    // Default perspective transforms
    protected static final ItemTransforms TRANSFORMS = ModelHelpers.modifyDefaultTransforms(ImmutableMap.of(
            ItemDisplayContext.GUI, new ItemTransform(
                    new Vector3f(30, 135, 0),
                    new Vector3f(0, 0, 0),
                    new Vector3f(0.625f, 0.625f, 0.625f))
    ));

    public static List<BakedQuad> boxModel;
    public static List<BakedQuad> boxLidModel;
    public static List<BakedQuad> boxLidRotatedModel;

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

        quads.addAll(boxModel);
        if (isOpen) {
            quads.addAll(boxLidRotatedModel);
        } else {
            quads.addAll(boxLidModel);
        }

        return quads;
    }

    @Override
    public List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction side, RandomSource rand, ModelData extraData, ChunkSectionLayer renderType) {
        return getGeneralQuads();
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
    public List<BakedQuad> handleItemState(@Nullable ItemStack itemStack, @Nullable Level world,
                                           @Nullable LivingEntity entity) {
        return new ModelBoxOfEternalClosureBaked(BlockBoxOfEternalClosure.getSpiritTypeWithFallbackSpirit(itemStack) == null,
                itemStack, world, entity).getGeneralQuads();
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        // TODO: fix breaking particle
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        try {
            return atlas.getSprite(ResourceLocation.withDefaultNamespace("missingno"));
        } catch (IllegalStateException e) {
            return null; // Can happen during game load
        }
    }

    @Override
    public boolean usesBlockLight() {
        return false; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
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
        return TRANSFORMS;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public String toString() {
        return "ModelBoxOfEternalClosureBaked(isOpen=" + this.isOpen() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ModelBoxOfEternalClosureBaked)) return false;
        final ModelBoxOfEternalClosureBaked other = (ModelBoxOfEternalClosureBaked) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isOpen() != other.isOpen()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ModelBoxOfEternalClosureBaked;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isOpen() ? 79 : 97);
        return result;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":box_of_eternal_closure";
    }
}
