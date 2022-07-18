package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.block.BlockEntangledChaliceConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A baked entangled chalice model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ModelEntangledChaliceBaked extends DelegatingDynamicItemAndBlockModel {

    private final static Map<String, Integer> seeds = Maps.newHashMap();

    public static final ResourceLocation chaliceModelName = new ResourceLocation(Reference.MOD_ID, "block/chalice");;
    public static final ResourceLocation gemsModelName = new ResourceLocation(Reference.MOD_ID, "block/gems");;

    public static BakedModel chaliceModel;
    public static BakedModel gemsModel;

    private final String id;
    private final FluidStack fluidStack;

    public ModelEntangledChaliceBaked() {
        super();
        id = "";
        fluidStack = null;
    }

    public ModelEntangledChaliceBaked(String id, FluidStack fluidStack, BlockState blockState, Direction facing, RandomSource rand, ModelData modelData, RenderType renderType) {
        super(blockState, facing, rand, modelData, renderType);
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
     * @param id Unique id of a chalice group.
     * @return The color seed
     */
    public static int getColorSeed(String id) {
        int gemColor;
        if(seeds.containsKey(id)) {
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
        quads.addAll(chaliceModel.getQuads(blockState, facing, rand));

        // Colored gems
        int color = getColorSeed(this.id);
        for(BakedQuad quad : gemsModel.getQuads(blockState, facing, rand)) {
            int[] data = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
            for(int i = 0; i < data.length / 8; i++) {
                data[i * 8 + 3] = color;
            }
            quads.add(new BakedQuad(data, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), false));
        }

        // Fluid
        if(!fluidStack.isEmpty()) {
            quads.addAll(getFluidQuads(fluidStack, BlockEntityEntangledChalice.BASE_CAPACITY));
        }

        return quads;
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData) {
        return BlockEntityHelpers.get(world, pos, BlockEntityEntangledChalice.class)
                .map(tile -> {
                    ModelData.Builder builder = ModelData.builder();
                    builder.with(BlockEntangledChalice.TANK_FLUID, tile.getTank().getFluid());
                    builder.with(BlockEntangledChalice.TANK_ID, tile.getWorldTankId());
                    return builder.build();
                })
                .orElse(ModelData.EMPTY);
    }

    @Override
    public BakedModel handleBlockState(BlockState state, Direction side, RandomSource rand, ModelData modelData, RenderType renderType) {
        String tankId = ModelHelpers.getSafeProperty(modelData, BlockEntangledChalice.TANK_ID, "");
        FluidStack fluidStack = ModelHelpers.getSafeProperty(modelData, BlockEntangledChalice.TANK_FLUID, FluidStack.EMPTY);
        if(!BlockEntangledChaliceConfig.staticBlockRendering) {
            fluidStack = FluidStack.EMPTY;
        }
        return new ModelEntangledChaliceBaked(tankId, fluidStack, state, side, rand, modelData, renderType);
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        String id = FluidUtil.getFluidHandler(itemStack)
                .map((h -> ((ItemEntangledChalice.FluidHandler) h).getTankID()))
                .orElse("");
        return new ModelEntangledChaliceBaked(id, FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY), itemStack, world, entity);
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return chaliceModel.getParticleIcon();
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(0.95F, ((float) fluidStack.getAmount() / (float) capacity)) * 0.1875F + 0.8125F;
        List<BakedQuad> quads = Lists.newArrayList();
        TextureAtlasSprite texture = org.cyclops.cyclopscore.helper.RenderHelpers.getFluidIcon(fluidStack, Direction.UP);
        int color = RenderHelpers.getFluidBakedQuadColor(fluidStack);
        addBakedQuadRotated(quads, 0.1875F, 0.8125F, 0.1875F, 0.8125F, height, texture, Direction.UP, ROTATION_FIX[Direction.UP.ordinal()], true, color, ROTATION_UV);
        return quads;
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public ItemTransforms getTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }
}
