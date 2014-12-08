package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.RegistryManager;
import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipeRegistry;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;
import evilcraft.client.gui.container.GuiBloodInfuser;
import evilcraft.client.particle.EntityBloodBubbleFX;
import evilcraft.core.config.configurable.ConfigurableBlockContainerGuiTankInfo;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.core.tileentity.WorkingTileEntity;
import evilcraft.inventory.container.ContainerBloodInfuser;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * A machine that can infuse stuff with blood.
 * @author rubensworks
 *
 */
public class BloodInfuser extends ConfigurableBlockContainerGuiTankInfo implements IMachine<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> {
    
    private static BloodInfuser _instance = null;
    
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
    public String getTankNBTName() {
        return TileBloodInfuser.TANKNAME;
    }

    @Override
    public int getMaxCapacity() {
        return TileBloodInfuser.LIQUID_PER_SLOT;
    }

    @Override
    public IRecipeRegistry<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> getRecipeRegistry() {
        return RegistryManager.getRegistry(ISuperRecipeRegistry.class).getRecipeRegistry(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        EntityBloodBubbleFX.randomDisplayTick((WorkingTileEntity) world.getTileEntity(x, y, z), world, x, y, z, random);
        super.randomDisplayTick(world, x, y, z, random);
    }
}
