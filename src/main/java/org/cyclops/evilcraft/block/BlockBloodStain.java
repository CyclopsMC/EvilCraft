package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodStain;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import java.util.Random;

/**
 * A blood stain that can rest on other blocks.
 * @author rubensworks
 */
public class BlockBloodStain extends BlockWithEntity {

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public BlockBloodStain(Block.Properties properties) {
        super(properties, BlockEntityBloodStain::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState p_225541_1_, Fluid p_225541_2_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);

        // Check if the block has a block entity
        if (!BlockBloodStainConfig.spawnOnBlockEntities && worldIn.getBlockEntity(blockpos) != null) {
            return false;
        }

        // Check if the block has been blacklisted
        String blockName = ForgeRegistries.BLOCKS.getKey(blockstate.getBlock()).toString();
        for (String blacklistedRegex : BlockBloodStainConfig.spawnBlacklist) {
            if (blockName.matches(blacklistedRegex)) {
                return false;
            }
        }

        return (blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP)
                && blockstate.isValidSpawn(worldIn, blockpos, SpawnPlacements.Type.ON_GROUND, EntityType.CHICKEN))
                || blockstate.getBlock() == Blocks.HOPPER;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (!state.canSurvive(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
        splash(worldIn, pos);
        super.attack(state, worldIn, pos, player);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.getDeltaMovement().length() > 0.1D) {
            splash(worldIn, pos);
        }
        super.entityInside(state, worldIn, pos, entityIn);
    }

    /**
     * Spawn particles.
     * @param world The world.
     * @param blockPos The position.
     */
    @OnlyIn(Dist.CLIENT)
    public static void splash(Level world, BlockPos blockPos) {
        if(world.isClientSide()) {
            ParticleBloodSplash.spawnParticles(world, blockPos, 1, 1 + world.random.nextInt(1));
        }
    }

    @Override
    public void handlePrecipitation(BlockState blockState, Level world, BlockPos blockPos, Biome.Precipitation precipitation) {
        world.removeBlock(blockPos, false);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void bloodStainedBlockEvent(LivingDeathEvent event) {
        if(event.getSource() == event.getEntity().damageSources().fall()
                && !(event.getEntity() instanceof EntityVengeanceSpirit)) {
            int x = Mth.floor(event.getEntity().getX());
            int y = Mth.floor(event.getEntity().getY());
            int z = Mth.floor(event.getEntity().getZ());

            if (!event.getEntity().level().isClientSide()) {
                event.getEntity().getServer().execute(() -> {
                    // Only in the next tick, to resolve #601.
                    // The problem is that Vanilla's logic for handling fall events caches the Block.
                    // But Forge throws the living death event _after_ this block is determined,
                    // after which vanilla can still perform operators with this block.
                    // In some cases, this can result in inconsistencies, which can lead to crashes.
                    BlockPos pos = new BlockPos(x, y - 1, z);
                    Block block = event.getEntity().level().getBlockState(pos).getBlock();

                    int amount = (int) (BlockBloodStainConfig.bloodMBPerHP * event.getEntity().getMaxHealth());
                    if (block != this) {
                        // Offset position by one
                        pos = pos.offset(0, 1, 0);

                        // Add a new blood stain block
                        if (event.getEntity().getCommandSenderWorld().isEmptyBlock(pos) && canSurvive(defaultBlockState(), event.getEntity().getCommandSenderWorld(), pos)) {
                            event.getEntity().getCommandSenderWorld().setBlockAndUpdate(pos, defaultBlockState());
                        }
                    }
                    // Add blood to existing block
                    BlockEntityHelpers.get(event.getEntity().getCommandSenderWorld(), pos, BlockEntityBloodStain.class)
                            .ifPresent(tile -> tile.addAmount(amount));
                });
            } else {
                // Init particles
                Random random = new Random();
                BlockPos pos = new BlockPos(x, y, z);
                ParticleBloodSplash.spawnParticles(event.getEntity().level(), pos.offset(0, 1, 0),
                        ((int) event.getEntity().getMaxHealth()) + random.nextInt(15), 5 + random.nextInt(5));
            }
        }
    }

}
