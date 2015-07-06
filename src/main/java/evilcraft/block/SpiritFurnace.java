package evilcraft.block;

import evilcraft.client.gui.container.GuiSpiritFurnace;
import evilcraft.core.block.CubeDetector.IDetectionListener;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.inventory.container.ContainerSpiritFurnace;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class SpiritFurnace extends ConfigurableBlockContainerGuiTankInfo implements IDetectionListener {
    
    private static SpiritFurnace _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SpiritFurnace(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritFurnace getInstance() {
        return _instance;
    }

    private SpiritFurnace(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSpiritFurnace.class);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
        this.setRotatable(true);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityplayer, EnumFacing side, float par7, float par8, float par9) {
        return !TileSpiritFurnace.canWork(world, blockPos) ||
                super.onBlockActivated(world, blockPos, blockState, entityplayer, side, par7, par8, par9);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public String getTankNBTName() {
        return TileSpiritFurnace.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }
    
    private void triggerDetector(World world, BlockPos blockPos, boolean valid) {
    	TileSpiritFurnace.detector.detect(world, blockPos, valid, true);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState blockState) {
    	triggerDetector(world, blockPos, true);
    }
    
    /*@Override
    public void onBlockPreDestroy(World world, BlockPos blockPos, IBlockState blockState) {
    	triggerDetector(world, blockPos, false);
    	super.onBlockPreDestroy(world, blockPos, meta);
    }*/

	@Override
	public void onDetect(World world, BlockPos location, Vec3i size, boolean valid) {
		Block block = world.getBlockState(location).getBlock();
		if(block == this) {
			TileSpiritFurnace.detectStructure(world, location, size, valid);
			TileEntity tile = world.getTileEntity(location);
			if(tile != null) {
				((TileSpiritFurnace) tile).setSize(valid ? size : Vec3i.NULL_VECTOR);
			}
		}
	}

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerSpiritFurnace.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiSpiritFurnace.class;
    }
}
