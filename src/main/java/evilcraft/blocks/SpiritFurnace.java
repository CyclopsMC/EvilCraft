package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.entities.tileentities.TileSpiritFurnace;
import evilcraft.gui.client.GuiSpiritFurnace;
import evilcraft.gui.container.ContainerSpiritFurnace;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class SpiritFurnace extends ConfigurableBlockContainerGuiTankInfo {
    
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
        if (side == ForgeDirection.SOUTH.ordinal()) {
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

}
