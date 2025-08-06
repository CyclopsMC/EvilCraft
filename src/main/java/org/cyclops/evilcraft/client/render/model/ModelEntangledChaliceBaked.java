package org.cyclops.evilcraft.client.render.model;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.block.BlockEntangledChaliceConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A baked entangled chalice model.
 *
 * @author rubensworks
 */
public class ModelEntangledChaliceBaked extends DelegatingDynamicItemAndBlockModel {

    private final static Map<String, Integer> seeds = Maps.newHashMap();

    public static final ResourceLocation chaliceModelName = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/chalice");
    public static final ResourceLocation gemsModelName = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/gems");

    public static BlockStateModel chaliceModel;
    public static BlockStateModel gemsModel;

    private final String id;
    private final FluidStack fluidStack;

    public ModelEntangledChaliceBaked() {
        super();
        id = "";
        fluidStack = null;
    }

    public ModelEntangledChaliceBaked(String id, FluidStack fluidStack, BlockAndTintGetter level, BlockState blockState, Direction facing, RandomSource rand, ModelData modelData, ChunkSectionLayer renderType) {
        super(level, blockState, facing, rand, modelData, renderType);
        this.id = id != null ? id : "";
        this.fluidStack = fluidStack;
    }

    public ModelEntangledChaliceBaked(String id, FluidStack fluidStack, ItemStack itemStack, Level world, LivingEntity entity) {
        super(itemStack, world, entity);
        this.id = id != null ? id : "";
        this.fluidStack = fluidStack;
    }

    /**
     * Set the color seed of the chalice.
     *
     * @param id Unique id of a chalice group.
     * @return The color seed
     */
    public static int getColorSeed(String id) {
        int gemColor;
        if (seeds.containsKey(id)) {
            gemColor = seeds.get(id);
        } else {
            long res = id.hashCode();
            Random rand = new Random(res);
            gemColor = rand.nextInt(1 << 24) | (255 << 24);
            seeds.put(id, gemColor);
        }
        return gemColor;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads = Lists.newLinkedList();

        // Base chalice model
        for (BlockModelPart blockModelPart : chaliceModel.collectParts(level, BlockPos.ZERO, blockState, rand)) {
            quads.addAll(blockModelPart.getQuads(null));
        }

        // Colored gems
        int color = getColorSeed(this.id);
        for (BlockModelPart blockModelPart : gemsModel.collectParts(level, BlockPos.ZERO, blockState, rand)) {
            for (BakedQuad quad : blockModelPart.getQuads(null)) {
                int[] data = Arrays.copyOf(quad.vertices(), quad.vertices().length);
                for (int i = 0; i < data.length / 8; i++) {
                    data[i * 8 + 3] = color;
                }
                quads.add(new BakedQuad(data, quad.tintIndex(), quad.direction(), quad.sprite(), false, 0, true));
            }
        }

        // Fluid
        if (!fluidStack.isEmpty()) {
            quads.addAll(getFluidQuads(fluidStack, BlockEntityEntangledChalice.BASE_CAPACITY));
        }

        return quads;
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData) {
        return IModHelpers.get().getBlockEntityHelpers().get(world, pos, BlockEntityEntangledChalice.class)
                .map(tile -> {
                    ModelData.Builder builder = ModelData.builder();
                    builder.with(BlockEntangledChalice.TANK_FLUID, tile.getTank().getFluid());
                    builder.with(BlockEntangledChalice.TANK_ID, tile.getWorldTankId());
                    return builder.build();
                })
                .orElse(ModelData.EMPTY);
    }

    @Override
    public List<ChunkSectionLayer> getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return List.of(ChunkSectionLayer.SOLID);
    }

    @Override
    public List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction side, RandomSource rand, ModelData extraData, ChunkSectionLayer renderType) {
        String tankId = ModelHelpers.getSafeProperty(modelData, BlockEntangledChalice.TANK_ID, "");
        FluidStack fluidStack = ModelHelpers.getSafeProperty(modelData, BlockEntangledChalice.TANK_FLUID, FluidStack.EMPTY);
        if (!BlockEntangledChaliceConfig.staticBlockRendering) {
            fluidStack = FluidStack.EMPTY;
        }
        return new ModelEntangledChaliceBaked(tankId, fluidStack, level, state, side, rand, modelData, renderType).getGeneralQuads();
    }

    @Override
    public List<BakedQuad> handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        String id = FluidUtil.getFluidHandler(itemStack)
                .map((h -> ((ItemEntangledChalice.FluidHandler) h).getTankID()))
                .orElse("");
        return new ModelEntangledChaliceBaked(id, FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY), itemStack, world, entity).getGeneralQuads();
    }

    @Override
    public TextureAtlasSprite particleIcon() {
        return chaliceModel.particleIcon();
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(0.95F, ((float) fluidStack.getAmount() / (float) capacity)) * 0.1875F + 0.8125F;
        List<BakedQuad> quads = Lists.newArrayList();
        TextureAtlasSprite texture = IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluidStack, Direction.UP);
        int color = IModHelpersNeoForge.get().getRenderHelpers().getFluidBakedQuadColor(fluidStack);
        addBakedQuadRotated(quads, 0.1875F, 0.8125F, 0.1875F, 0.8125F, height, texture, Direction.UP, ROTATION_FIX[Direction.UP.ordinal()], true, color, ROTATION_UV);
        return quads;
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public UnbakedModel wrapped() {
        return null;
    }

    @Override
    public @Nullable ResolvedModel parent() {
        return null;
    }

    @Override
    public ItemTransforms getTopTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    public String getId() {
        return this.id;
    }

    public FluidStack getFluidStack() {
        return this.fluidStack;
    }

    public String toString() {
        return "ModelEntangledChaliceBaked(id=" + this.getId() + ", fluidStack=" + this.getFluidStack() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ModelEntangledChaliceBaked)) return false;
        final ModelEntangledChaliceBaked other = (ModelEntangledChaliceBaked) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$fluidStack = this.getFluidStack();
        final Object other$fluidStack = other.getFluidStack();
        if (this$fluidStack == null ? other$fluidStack != null : !this$fluidStack.equals(other$fluidStack))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ModelEntangledChaliceBaked;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $fluidStack = this.getFluidStack();
        result = result * PRIME + ($fluidStack == null ? 43 : $fluidStack.hashCode());
        return result;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":entangled_chalice";
    }
}
