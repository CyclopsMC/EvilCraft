package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.client.particle.ParticleBloodBubble;
import org.cyclops.evilcraft.core.block.BlockTileGuiTank;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.tileentity.TileSpiritReanimator;

import java.util.Random;

/**
 * A machine that can reanimate spirits inside BOEC's.
 * @author rubensworks
 *
 */
public class BlockSpiritReanimator extends BlockTileGuiTank {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty ON = BooleanProperty.create("on");

    public BlockSpiritReanimator(Block.Properties properties) {
        super(properties, TileSpiritReanimator::new);
    }

    @Override
    public int getDefaultCapacity() {
        return TileSpiritReanimator.LIQUID_PER_SLOT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        ParticleBloodBubble.randomDisplayTick((WorkingTileEntity) world.getTileEntity(blockPos), world, blockPos,
                random, BlockHelpers.getSafeBlockStateProperty(blockState, FACING, Direction.NORTH));
        super.animateTick(blockState, world, blockPos, random);
    }

    /*
    @Override
    public Class<? extends Container> getContainer() {
        return ContainerSpiritReanimator.class;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Class<? extends Screen> getGui() {
        return GuiSpiritReanimator.class;
    }
    TODO: gui
    */
}
