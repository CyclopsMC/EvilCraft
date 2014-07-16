package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.algorithms.ILocation;
import evilcraft.api.algorithms.Location;
import evilcraft.api.algorithms.Locations;
import evilcraft.api.algorithms.Size;
import evilcraft.api.block.CubeDetector.IDetectionListener;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.entities.tileentities.TileSpiritFurnace;
import evilcraft.gui.client.GuiSpiritFurnace;
import evilcraft.gui.container.ContainerSpiritFurnace;
import evilcraft.network.PacketHandler;
import evilcraft.network.packets.DetectionListenerPacket;

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
        this.setStepSound(soundTypeStone);
        this.setRotatable(true);
        
        if (Helpers.isClientSide())
            setGUI(GuiSpiritFurnace.class);
        setContainer(ContainerSpiritFurnace.class);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(getTextureName());
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    	TileSpiritFurnace tile = (TileSpiritFurnace) world.getTileEntity(x, y, z);
        ForgeDirection rotatedDirection = Helpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), tile.isBlockCooking()?1:0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == ForgeDirection.SOUTH.ordinal() && meta == 0) {
            return this.blockIcon;
        } else {
            return DarkBloodBrick.getInstance().getIcon(side, meta);
        }
    }
    
    @Override
    public Item getItemDropped(int par1, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
            return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
    	TileSpiritFurnace tile = (TileSpiritFurnace) world.getTileEntity(x, y, z);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(Helpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    public String getTankNBTName() {
        return TileSpiritFurnace.TANKNAME;
    }

    @Override
    public int getTankCapacity() {
        return TileSpiritFurnace.LIQUID_PER_SLOT;
    }
    
    private void triggerDetector(World world, int x, int y, int z, boolean valid) {
    	TileSpiritFurnace.detector.detect(world, new Location(new int[]{x, y, z}), valid);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	triggerDetector(world, x, y, z, true);
    }
    
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
    	triggerDetector(world, x, y, z, false);
    	super.onBlockPreDestroy(world, x, y, z, meta);
    }
    
    /**
     * Callback for when a structure has been detected for a spirit furnace block.
     * @param world The world.
     * @param location The location of one block of the structure.
     * @param size The size of the structure.
     * @param valid If the structure is being validated(/created), otherwise invalidated.
     */
    public static void detectStructure(World world, ILocation location, Size size, boolean valid) {
    	int newMeta = valid ? 1 : 0;
		boolean change = Locations.getBlockMeta(world, location) != newMeta;
		Locations.setBlockMetadata(world, location, newMeta, Helpers.BLOCK_NOTIFY_CLIENT);
		TileEntity tile = Locations.getTile(world, location);
		if(change) {
			PacketHandler.sendToServer(new DetectionListenerPacket(location, valid));
		}
    }

	@Override
	public void onDetect(World world, ILocation location, Size size, boolean valid) {
		Block block = Locations.getBlock(world, location);
		if(block == this) {
			detectStructure(world, location, size, valid);
			// TODO: notify TE.
			System.out.println("Found a furnace!:" + location + "; valid?"+valid);
		}
	}

}
