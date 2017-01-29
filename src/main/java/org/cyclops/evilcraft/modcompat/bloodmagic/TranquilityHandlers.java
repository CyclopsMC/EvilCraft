package org.cyclops.evilcraft.modcompat.bloodmagic;

import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.ITranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.*;

/**
 * Tranquility handlers for Blood Magic
 * @author rubensworks
 */
public class TranquilityHandlers {

    public static class TreeLog implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(UndeadLogConfig.class) && block == UndeadLogConfig._instance.getBlockInstance()) {
                return new TranquilityStack(EnumTranquilityType.TREE, 1.2);
            }
            return null;
        }
    }

    public static class TreeLeaves implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(UndeadLeavesConfig.class) && block == UndeadLeaves.getInstance()) {
                return new TranquilityStack(EnumTranquilityType.PLANT, 1.2);
            }
            return null;
        }
    }

    public static class Planks implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(UndeadPlankConfig.class) && block == UndeadPlankConfig._instance.getBlockInstance()) {
                return new TranquilityStack(EnumTranquilityType.PLANT, 0.8);
            }
            return null;
        }
    }

    public static class Blood implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(org.cyclops.evilcraft.fluid.BloodConfig.class) && block == org.cyclops.evilcraft.fluid.Blood.getInstance().getBlock()) {
                return new TranquilityStack(EnumTranquilityType.WATER, 1.6);
            }
            return null;
        }
    }

    public static class Poison implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(org.cyclops.evilcraft.fluid.PoisonConfig.class) && block == org.cyclops.evilcraft.fluid.Poison.getInstance().getBlock()) {
                return new TranquilityStack(EnumTranquilityType.WATER, 0.8);
            }
            return null;
        }
    }

    public static class HardenedBlood implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(HardenedBloodConfig.class) && block == org.cyclops.evilcraft.block.HardenedBlood.getInstance()) {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 1.5);
            }
            return null;
        }
    }

    public static class DarkBricks implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(DarkBrickConfig.class) && block == DarkBrickConfig._instance.getBlockInstance()) {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 1);
            }
            return null;
        }
    }

    public static class DarkBloodBricks implements ITranquilityHandler {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
            if (Configs.isEnabled(DarkBloodBrickConfig.class) && block == DarkBloodBrickConfig._instance.getBlockInstance()) {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 1.5);
            }
            return null;
        }
    }

}
