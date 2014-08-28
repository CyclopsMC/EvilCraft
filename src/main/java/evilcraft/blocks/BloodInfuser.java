package evilcraft.blocks;
import java.util.Random;

import evilcraft.api.recipes.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.api.helpers.DirectionHelpers;
import evilcraft.api.helpers.MinecraftHelpers;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.GuiBloodInfuser;
import evilcraft.gui.container.ContainerBloodInfuser;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BloodInfuser extends ConfigurableBlockContainerGuiTankInfo implements IMachine<BloodInfuser, ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> {
    
    private static BloodInfuser _instance = null;
    
    private IIcon sideIcon;
    private IIcon topIcon;
    private IIcon frontIconOn;
    private IIcon frontIconOff;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodInfuser(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodInfuser getInstance() {
        return _instance;
    }

    private BloodInfuser(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock, TileBloodInfuser.class);
        this.setStepSound(soundTypeStone);
        this.setRotatable(true);
        
        if (MinecraftHelpers.isClientSide())
            setGUI(GuiBloodInfuser.class);
        setContainer(ContainerBloodInfuser.class);
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
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getTileEntity(x, y, z);
        ForgeDirection rotatedDirection = DirectionHelpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), tile.isVisuallyWorking()?1:0);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
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
    public Item getItemDropped(int par1, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
            return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileBloodInfuser tile = (TileBloodInfuser) world.getTileEntity(x, y, z);
        float output = (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity();
        return (int)Math.ceil(MinecraftHelpers.COMPARATOR_MULTIPLIER * output);
    }

    @Override
    public String getTankNBTName() {
        return TileBloodInfuser.TANKNAME;
    }

    @Override
    public int getTankCapacity() {
        return TileBloodInfuser.LIQUID_PER_SLOT;
    }

    @Override
    public SuperRecipeRegistry.RecipeRegistry<BloodInfuser, ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> getRecipeRegistry() {
        return SuperRecipeRegistry.getRecipeRegistry(this);
    }
}
