package evilcraft.block;

import evilcraft.client.gui.container.GuiSpiritReanimator;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.inventory.container.ContainerSpiritReanimator;
import evilcraft.tileentity.TileSpiritReanimator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

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
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
        this.setRotatable(true);
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
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random random) {
        EntityBloodBubbleFX.randomDisplayTick((WorkingTileEntity) world.getTileEntity(blockPos), world, blockPos, random);
        super.randomDisplayTick(world, blockPos, blockState, random);
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
