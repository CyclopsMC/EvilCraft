package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.client.model.DelegatingDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.block.BlockEntangledChaliceConfig;
import org.cyclops.evilcraft.item.ItemEntangledChalice;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

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

    public static IBakedModel chaliceModel;
    public static IBakedModel gemsModel;

    private final String id;
    private final FluidStack fluidStack;

    public ModelEntangledChaliceBaked() {
        super();
        id = "";
        fluidStack = null;
    }

    public ModelEntangledChaliceBaked(String id, FluidStack fluidStack, BlockState blockState, Direction facing, Random rand, IModelData modelData) {
        super(blockState, facing, rand, modelData);
        this.id = id != null ? id : "";
        this.fluidStack = fluidStack;
    }

    public ModelEntangledChaliceBaked(String id, FluidStack fluidStack, ItemStack itemStack, World world, LivingEntity entity) {
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
            int[] data = Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length);
            for(int i = 0; i < data.length / 8; i++) {
                data[i * 8 + 3] = color;
            }
            quads.add(new BakedQuad(data, quad.getTintIndex(), quad.getFace(), quad.getSprite(), false));
        }

        // Fluid
        if(!fluidStack.isEmpty()) {
            quads.addAll(getFluidQuads(fluidStack, TileEntangledChalice.BASE_CAPACITY));
        }

        return quads;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        return TileHelpers.getSafeTile(world, pos, TileEntangledChalice.class)
                .map(tile -> {
                    ModelDataMap.Builder builder = new ModelDataMap.Builder();
                    builder.withInitial(BlockEntangledChalice.TANK_FLUID, tile.getTank().getFluid());
                    builder.withInitial(BlockEntangledChalice.TANK_ID, tile.getWorldTankId());
                    return (IModelData) builder.build();
                })
                .orElse(EmptyModelData.INSTANCE);
    }

    @Override
    public IBakedModel handleBlockState(BlockState state, Direction side, Random rand, IModelData modelData) {
        String tankId = ModelHelpers.getSafeProperty(modelData, BlockEntangledChalice.TANK_ID, "");
        FluidStack fluidStack = ModelHelpers.getSafeProperty(modelData, BlockEntangledChalice.TANK_FLUID, FluidStack.EMPTY);
        if(!BlockEntangledChaliceConfig.staticBlockRendering) {
            fluidStack = FluidStack.EMPTY;
        }
        return new ModelEntangledChaliceBaked(tankId, fluidStack, state, side, rand, modelData);
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        String id = FluidUtil.getFluidHandler(itemStack)
                .map((h -> ((ItemEntangledChalice.FluidHandler) h).getTankID()))
                .orElse("");
        return new ModelEntangledChaliceBaked(id, FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY), itemStack, world, entity);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return chaliceModel.getParticleTexture();
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
    public boolean isSideLit() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }
}
