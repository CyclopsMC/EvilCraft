package evilcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.tileentity.TileFireBlaster;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Block that blast fire depending on its redstone signal.
 *
 * @author rubensworks
 */
public class FireBlaster extends ConfigurableBlockContainer {

    private static FireBlaster _instance = null;

    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private IIcon frontIcon;

    /**
     * Initialise the configurable.
     *
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if (_instance == null)
            _instance = new FireBlaster(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     *
     * @return The instance.
     */
    public static FireBlaster getInstance() {
        return _instance;
    }

    private FireBlaster(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileFireBlaster.class);
        this.setHardness(2F);
        this.setStepSound(soundTypeMetal);
        this.setRotatable(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        topIcon = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.UP.name());
        sideIcon = iconRegister.registerIcon(getTextureName() + "_" + "side");
        frontIcon = iconRegister.registerIcon(getTextureName() + "_" + ForgeDirection.NORTH.name());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileFireBlaster tile = (TileFireBlaster) world.getTileEntity(x, y, z);
        ForgeDirection rotatedDirection = DirectionHelpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
        return getIcon(rotatedDirection.ordinal(), 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if(side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) {
            return topIcon;
        } else if (side == ForgeDirection.SOUTH.ordinal()) {
            return frontIcon;
        } else {
            return sideIcon;
        }
    }

    @Override
    protected boolean getRotationHorizontalOnly() {
        return false;
    }

}
