package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.gui.container.GuiSanguinaryEnvironmentalAccumulator;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGui;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class SanguinaryEnvironmentalAccumulator extends ConfigurableBlockContainerGui {

    private static SanguinaryEnvironmentalAccumulator _instance = null;

    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIconOn;
    @SideOnly(Side.CLIENT)
    private IIcon topIconOff;
    @SideOnly(Side.CLIENT)
    private IIcon downIcon;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SanguinaryEnvironmentalAccumulator(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryEnvironmentalAccumulator getInstance() {
        return _instance;
    }

    private SanguinaryEnvironmentalAccumulator(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileSanguinaryEnvironmentalAccumulator.class);
        this.setStepSound(soundTypeStone);
        this.setRotatable(true);
        
        if (MinecraftHelpers.isClientSide())
            setGUI(GuiSanguinaryEnvironmentalAccumulator.class);
        setContainer(ContainerSanguinaryEnvironmentalAccumulator.class);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        topIconOn = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.UP.name() + "_on");
        topIconOff = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.UP.name() + "_off");
        sideIcon = iconRegister.registerIcon(getTextureName() + "_" + "side");
        downIcon = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.DOWN.name());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileSanguinaryEnvironmentalAccumulator tile = (TileSanguinaryEnvironmentalAccumulator) world.getTileEntity(x, y, z);
        ForgeDirection rotatedDirection = DirectionHelpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), tile.isVisuallyWorking()?1:0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if(side == ForgeDirection.DOWN.ordinal()) {
            return downIcon;
        } else if (side == ForgeDirection.UP.ordinal()) {
            if(meta == 1) {
                return topIconOn;
            } else {
                return topIconOff;
            }
        } else {
            return sideIcon;
        }
    }
    
    @Override
    public Item getItemDropped(int par1, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileSanguinaryEnvironmentalAccumulator tile = (TileSanguinaryEnvironmentalAccumulator) world.getTileEntity(x, y, z);
        return tile.isVisuallyWorking() ? 4 : super.getLightValue(world, x, y, z);
    }
}
