package evilcraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;
import evilcraft.entities.tileentities.TileEnvironmentalAccumulator;

public class EnvironmentalAccumulator extends ConfigurableBlockContainer {
	
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
    
    public static final int BEAM_ACTIVE = 0;
    public static final int BEAM_INACTIVE = 1;
    
    private Icon sideIcon;
    private Icon bottomIcon;
    private Icon topIcon;

	public EnvironmentalAccumulator(ExtendedConfig eConfig) {
		super(eConfig, Material.iron, TileEnvironmentalAccumulator.class);
		this.setRotatable(true);
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
}
