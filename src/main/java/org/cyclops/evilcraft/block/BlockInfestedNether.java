package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityNetherfish;

import javax.annotation.Nullable;

/**
 * A blockState that spawns a {@link EntityNetherfish} when the blockState breaks.
 * @author rubensworks
 *
 */
public class BlockInfestedNether extends Block {

    private final BlockInfestedNether.Type type;

    public BlockInfestedNether(Block.Properties properties, BlockInfestedNether.Type type) {
        super(properties);
        this.type = type;
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        if (!world.isRemote()) {
            EntityNetherfish netherfish = new EntityNetherfish((World) world);
            netherfish.setLocationAndAngles((double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(netherfish);
            netherfish.spawnExplosionParticle();
        }

        super.onPlayerDestroy(world, blockPos, blockState);
    }

    @Nullable
    public static Type unwrapBlock(BlockState blockState) {
        if (blockState.getBlock() == Blocks.NETHERRACK) {
            return Type.NETHERRACK;
        }
        if (blockState.getBlock() == Blocks.NETHER_BRICKS) {
            return Type.NETHER_BRICKS;
        }
        if (blockState.getBlock() == Blocks.SOUL_SAND) {
            return Type.SOUL_SAND;
        }
        return null;
    }

    public static Block wrapBlock(Type type) {
        switch (type) {
            case NETHERRACK:
                return RegistryEntries.BLOCK_INFESTED_NETHER_NETHERRACK;
            case NETHER_BRICKS:
                return RegistryEntries.BLOCK_INFESTED_NETHER_NETHER_BRICK;
            case SOUL_SAND:
                return RegistryEntries.BLOCK_INFESTED_NETHER_SOUL_SAND;
        }
        return null;
    }

    public static enum Type {
        NETHERRACK,
        NETHER_BRICKS,
        SOUL_SAND
    }

}
