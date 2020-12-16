package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.item.ItemDarkGem;

import java.util.List;
import java.util.Random;

/**
 * Ore that drops {@link ItemDarkGem}.
 * @author rubensworks
 *
 */
public class BlockDarkOre extends Block implements IInformationProvider {

    private static final int INCREASE_XP = 5; // Amount of XP that can be gained from mining this blockState

    public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

    public BlockDarkOre(Block.Properties properties) {
        super(properties);

        this.setDefaultState(this.stateContainer.getBaseState()
                .with(GLOWING, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(GLOWING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(GLOWING, false);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? 1 + RANDOM.nextInt(INCREASE_XP) : 0;
    }

    @Override
    public int tickRate(IWorldReader worldIn) {
        return 30;
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        this.glow(worldIn, pos);
        super.onBlockClicked(state, worldIn, pos, player);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        this.glow(world, pos);
        super.onEntityWalk(world, pos, entity);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        this.glow(worldIn, pos);
        return super.onBlockActivated(state, worldIn, pos, player, handIn, p_225533_6_);
    }
    
    private boolean isGlowing(World world, BlockPos blockPos) {
        return BlockHelpers.getSafeBlockStateProperty(world.getBlockState(blockPos), GLOWING, true);
    }

    private void glow(World world, BlockPos blockPos) {
    	if (world.isRemote())
            this.sparkle(world, blockPos);

        if (!isGlowing(world, blockPos)) {
            world.setBlockState(blockPos, getDefaultState().with(GLOWING, true), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos blockPos, Random rand) {
        if (isGlowing(world, blockPos)) {
            world.setBlockState(blockPos, getDefaultState().with(GLOWING, false), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World world, BlockPos blockPos, Random rand) {
        if (isGlowing(world, blockPos)) {
            this.sparkle(world, blockPos);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void sparkle(World world, BlockPos blockPos) {
        Random random = world.rand;
        double offset = 0.0625D;

        for (int l = 0; l < 6; ++l) {
            double sparkX = (double)((float)blockPos.getX() + random.nextFloat());
            double sparkY = (double)((float)blockPos.getY() + random.nextFloat());
            double sparkZ = (double)((float)blockPos.getZ() + random.nextFloat());

            if (l == 0 && !world.getBlockState(blockPos.add(0, 1, 0)).isNormalCube(world, blockPos.add(0, 1, 0))) {
                sparkY = (double)(blockPos.getY() + 1) + offset;
            }

            if (l == 1 && !world.getBlockState(blockPos.add(0, -1, 0)).isNormalCube(world, blockPos.add(0, -1, 0))) {
                sparkY = (double)(blockPos.getY()) - offset;
            }

            if (l == 2 && !world.getBlockState(blockPos.add(0, 0, 1)).isNormalCube(world, blockPos.add(0, 0, 1))) {
                sparkZ = (double)(blockPos.getZ() + 1) + offset;
            }

            if (l == 3 && !world.getBlockState(blockPos.add(0, 0, -1)).isNormalCube(world, blockPos.add(0, 0, -1))) {
                sparkZ = (double)(blockPos.getZ()) - offset;
            }

            if (l == 4 && !world.getBlockState(blockPos.add(1, 0, 0)).isNormalCube(world, blockPos.add(1, 0, 0))) {
                sparkX = (double)(blockPos.getX() + 1) + offset;
            }

            if (l == 5 && !world.getBlockState(blockPos.add(-1, 0, 0)).isNormalCube(world, blockPos.add(-1, 0, 0))) {
                sparkX = (double)(blockPos.getX()) - offset;
            }

            if (sparkX < (double)blockPos.getX()
                    || sparkX > (double)(blockPos.getX() + 1)
                    || sparkY < 0.0D
                    || sparkY > (double)(blockPos.getY() + 1)
                    || sparkZ < (double)blockPos.getZ()
                    || sparkZ > (double)(blockPos.getZ() + 1)) {
                world.addParticle(ParticleTypes.SMOKE, sparkX, sparkY, sparkZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public ITextComponent getInfo(ItemStack itemStack) {
    	return new TranslationTextComponent(this.getTranslationKey()
                + ".info.custom", BlockDarkOreConfig.endY)
                .applyTextStyles(INFO_PREFIX_STYLES);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag iTooltipFlag) {

    }

}
