package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.gui.container.GuiSpiritReanimator;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.inventory.container.ContainerSpiritReanimator;
import evilcraft.tileentity.TileSpiritReanimator;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * A machine that can reanimate spirits inside BOEC's.
 * @author rubensworks
 *
 */
public class SpiritReanimator extends ConfigurableBlockContainerGuiTankInfo {
    
    private static SpiritReanimator _instance = null;
    
    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon frontIconOn;
    @SideOnly(Side.CLIENT)
    private IIcon frontIconOff;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SpiritReanimator(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritReanimator getInstance() {
        return _instance;
    }

    private SpiritReanimator(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSpiritReanimator.class);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
        this.setRotatable(true);
        
        if (MinecraftHelpers.isClientSide())
            setGUI(GuiSpiritReanimator.class);
        setContainer(ContainerSpiritReanimator.class);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    	TileSpiritReanimator tile = (TileSpiritReanimator) world.getTileEntity(x, y, z);
        ForgeDirection rotatedDirection = DirectionHelpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), tile.isVisuallyWorking()?1:0);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        topIcon = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.UP.name());
        sideIcon = iconRegister.registerIcon(getTextureName() + "_" + "side");
        frontIconOn = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name() + "_on");
        frontIconOff = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name() + "_off");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if(side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) {
            return topIcon;
        } else if (side == ForgeDirection.SOUTH.ordinal()) {
        	return meta == 1 ? frontIconOn : frontIconOff;
        } else {
            return sideIcon;
        }
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
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        EntityBloodBubbleFX.randomDisplayTick((WorkingTileEntity) world.getTileEntity(x, y, z), world, x, y, z, random);
        super.randomDisplayTick(world, x, y, z, random);
    }

}
