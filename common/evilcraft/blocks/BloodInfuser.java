package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.Helpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.GuiBloodInfuser;
import evilcraft.gui.container.ContainerBloodInfuser;

public class BloodInfuser extends ConfigurableBlockContainerGuiTankInfo {
    
    private static BloodInfuser _instance = null;
    
    private Icon sideIcon;
    private Icon topIcon;
    private Icon frontIconOn;
    private Icon frontIconOff;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodInfuser(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodInfuser getInstance() {
        return _instance;
    }

    private BloodInfuser(ExtendedConfig eConfig) {
        super(eConfig, Material.ground, TileBloodInfuser.class, Reference.GUI_BLOOD_INFUSER);
        this.setRotatable(true);
        
        if (Helpers.isClientSide())
            setGUI(GuiBloodInfuser.class);
        setContainer(ContainerBloodInfuser.class);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        topIcon = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.UP.name());
        sideIcon = iconRegister.registerIcon(getTextureName() + "_" + "side");
        frontIconOn = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name() + "_on");
        frontIconOff = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name() + "_off");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getBlockTileEntity(x, y, z);
        ForgeDirection rotatedDirection = Helpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), tile.isInfusing()?1:0);
    }
    
    @SideOnly(Side.CLIENT)
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
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return BloodInfuserConfig._instance.ID;
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
        return TileBloodInfuser.TANKNAME;
    }

    @Override
    public int getTankCapacity() {
        return TileBloodInfuser.LIQUID_PER_SLOT;
    }

}
