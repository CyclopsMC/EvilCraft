package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.client.gui.container.GuiSpiritReanimator;
import org.cyclops.evilcraft.client.particle.EntityBloodBubbleFX;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import org.cyclops.evilcraft.core.tileentity.WorkingTileEntity;
import org.cyclops.evilcraft.inventory.container.ContainerSpiritReanimator;
import org.cyclops.evilcraft.tileentity.TileSpiritReanimator;

import java.util.Random;

/**
 * A machine that can reanimate spirits inside BOEC's.
 * @author rubensworks
 *
 */
public class SpiritReanimator extends ConfigurableBlockContainerGuiTankInfo {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    @BlockProperty
    public static final PropertyBool ON = PropertyBool.create("on");
    
    private static SpiritReanimator _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritReanimator getInstance() {
        return _instance;
    }

    public SpiritReanimator(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSpiritReanimator.class);
        this.setHardness(5.0F);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
        this.setRotatable(true);
    }

    @Override
    public SoundType getStepSound() {
        return SoundType.STONE;
    }

    @Override
    public String getTankNBTName() {
        return TileSpiritReanimator.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileSpiritReanimator.LIQUID_PER_SLOT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random random) {
        EntityBloodBubbleFX.randomDisplayTick((WorkingTileEntity) world.getTileEntity(blockPos), world, blockPos,
                random, BlockHelpers.getSafeBlockStateProperty(blockState, FACING, EnumFacing.NORTH));
        super.randomDisplayTick(blockState, world, blockPos, random);
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerSpiritReanimator.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiSpiritReanimator.class;
    }
}
