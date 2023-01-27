package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.item.ItemDarkGem;

import java.util.List;

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

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(GLOWING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLOWING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(GLOWING, false);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, RandomSource randomSource, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? 1 + randomSource.nextInt(INCREASE_XP) : 0;
    }

    @Override
    public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
        this.glow(worldIn, pos);
        super.attack(state, worldIn, pos, player);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState blockState, Entity entity) {
        this.glow(world, pos);
        super.stepOn(world, pos, blockState, entity);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        this.glow(worldIn, pos);
        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    private boolean isGlowing(Level world, BlockPos blockPos) {
        return BlockHelpers.getSafeBlockStateProperty(world.getBlockState(blockPos), GLOWING, true);
    }

    private void glow(Level world, BlockPos blockPos) {
        if (world.isClientSide())
            this.sparkle(world, blockPos);

        if (!isGlowing(world, blockPos)) {
            world.setBlock(blockPos, defaultBlockState().setValue(GLOWING, true), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(GLOWING);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos blockPos, RandomSource rand) {
        if (isGlowing(world, blockPos)) {
            world.setBlock(blockPos, defaultBlockState().setValue(GLOWING, false), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level world, BlockPos blockPos, RandomSource rand) {
        if (isGlowing(world, blockPos)) {
            this.sparkle(world, blockPos);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void sparkle(Level world, BlockPos blockPos) {
        RandomSource random = world.random;
        double offset = 0.0625D;

        for (int l = 0; l < 6; ++l) {
            double sparkX = (double)((float)blockPos.getX() + random.nextFloat());
            double sparkY = (double)((float)blockPos.getY() + random.nextFloat());
            double sparkZ = (double)((float)blockPos.getZ() + random.nextFloat());

            if (l == 0 && !world.getBlockState(blockPos.offset(0, 1, 0)).isRedstoneConductor(world, blockPos.offset(0, 1, 0))) {
                sparkY = (double)(blockPos.getY() + 1) + offset;
            }

            if (l == 1 && !world.getBlockState(blockPos.offset(0, -1, 0)).isRedstoneConductor(world, blockPos.offset(0, -1, 0))) {
                sparkY = (double)(blockPos.getY()) - offset;
            }

            if (l == 2 && !world.getBlockState(blockPos.offset(0, 0, 1)).isRedstoneConductor(world, blockPos.offset(0, 0, 1))) {
                sparkZ = (double)(blockPos.getZ() + 1) + offset;
            }

            if (l == 3 && !world.getBlockState(blockPos.offset(0, 0, -1)).isRedstoneConductor(world, blockPos.offset(0, 0, -1))) {
                sparkZ = (double)(blockPos.getZ()) - offset;
            }

            if (l == 4 && !world.getBlockState(blockPos.offset(1, 0, 0)).isRedstoneConductor(world, blockPos.offset(1, 0, 0))) {
                sparkX = (double)(blockPos.getX() + 1) + offset;
            }

            if (l == 5 && !world.getBlockState(blockPos.offset(-1, 0, 0)).isRedstoneConductor(world, blockPos.offset(-1, 0, 0))) {
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
    public MutableComponent getInfo(ItemStack itemStack) {
        return Component.translatable(this.getDescriptionId()
                + ".info.custom", 66)
                .withStyle(INFO_PREFIX_STYLES);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, Level world, List<Component> list, TooltipFlag iTooltipFlag) {

    }

}
