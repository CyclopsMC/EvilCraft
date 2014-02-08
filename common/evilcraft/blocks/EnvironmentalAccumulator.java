package evilcraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;

public class EnvironmentalAccumulator extends ConfigurableBlockContainer implements IInformationProvider {
	
	private static EnvironmentalAccumulator _instance = null;
	
	public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new EnvironmentalAccumulator(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static EnvironmentalAccumulator getInstance() {
        return _instance;
    }
    
    // First bit of metadata indicates the beam is active
    // 0: beam active, 1: beam inactive
    public static final int BEAM_ACTIVE = 0;
    public static final int BEAM_INACTIVE = 1;
    
    // Second bit indicates wether or not we're moving an item in the beam
    // 0: not moving, 1: moving
    public static final int MOVING_ITEM = 2;
    
    private Icon sideIcon;
    private Icon bottomIcon;
    private Icon topIcon;

	public EnvironmentalAccumulator(ExtendedConfig eConfig) {
		super(eConfig, Material.iron, TileEnvironmentalAccumulator.class);
		this.setRotatable(true);
		this.setStepSound(soundMetalFootstep);
		this.setHardness(50.0F);
		this.setResistance(6000000.0F);   // Can not be destroyed by explosions
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
	    return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta) {
	    if (side == ForgeDirection.UP.ordinal())
	        return topIcon;
	    
	    if (side == ForgeDirection.DOWN.ordinal())
	        return bottomIcon;
	    
	    return sideIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister) {
	    sideIcon = iconRegister.registerIcon(getTextureName() + "_side");
	    bottomIcon = iconRegister.registerIcon(getTextureName() + "_bottom");
	    topIcon = iconRegister.registerIcon(getTextureName() + "_top");
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
	    // Environmental Accumulators should not drop upon breaking
	    world.setBlockToAir(x, y, z);
	}
	
	@Override
    public String getInfo(ItemStack itemStack) {
        return IInformationProvider.INFO_PREFIX + "Found at Dark Temples, high in the mountains.";
    }

    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}
    
    public static boolean isBeamActive(int metadata) {
        return (metadata & EnvironmentalAccumulator.BEAM_INACTIVE) == 0;
    }
    
    public static boolean isMovingItem(int metadata) {
        return (metadata & EnvironmentalAccumulator.MOVING_ITEM) == EnvironmentalAccumulator.MOVING_ITEM;
    }
    
    public static boolean isDoneMovingItem(int metadata) {
        return !isBeamActive(metadata) && isMovingItem(metadata);
    }
}
