package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.GuiBloodChest;
import evilcraft.gui.container.ContainerBloodChest;

public class BloodChest extends ConfigurableBlockContainerGuiTankInfo {
    
    private static BloodChest _instance = null;
    
    private Icon sideIcon;
    private Icon topIcon;
    private Icon frontIconOn;
    private Icon frontIconOff;
    
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodChest(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodChest getInstance() {
        return _instance;
    }

    private BloodChest(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.ground, TileBloodChest.class, Reference.GUI_BLOOD_CHEST);
        this.setRotatable(true);
        setGUI(ContainerBloodChest.class, GuiBloodChest.class);
    }
    
    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        topIcon = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.UP.name());
        sideIcon = iconRegister.registerIcon(getTextureName() + "_" + "side");
        frontIconOn = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name() + "_on");
        frontIconOff = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name() + "_off");
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getBlockTileEntity(x, y, z);
        ForgeDirection rotatedDirection = Helpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), tile.isInfusing()?1:0);
    }
    
    @Override
    public Icon getIcon(int side, int meta) {
        if(side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) {
            return topIcon;
        } else if (side == ForgeDirection.SOUTH.ordinal()) {
            if(meta == 1) {
                return frontIconOn;
            } else {
                return frontIconOff;
            }
        } else {
            return sideIcon;
        }
    }*/
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return BloodChestConfig._instance.ID;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
            return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getBlockTileEntity(x, y, z);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(Helpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    public String getTankNBTName() {
        return TileBloodChest.TANKNAME;
    }

    @Override
    public int getTankCapacity() {
        return TileBloodChest.LIQUID_PER_SLOT;
    }

}
