package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.api.render.ConnectableIcon;

public class ObscuredGlass extends ConfigurableBlockConnectedTexture {
    
    protected Icon blockIconInventory;
    
    private static ObscuredGlass _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new ObscuredGlass(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static ObscuredGlass getInstance() {
        return _instance;
    }

    private ObscuredGlass(ExtendedConfig eConfig) {
        super(eConfig, Material.glass);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGlassFootstep);
        this.setLightOpacity(10);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        blockIconInventory = iconRegister.registerIcon(getTextureName()+"_inventory");
    }
    
    @Override
    public Icon getIconInventory() {
        return blockIconInventory;
    }
    
    @Override
    public int idDropped(int par1, Random random, int zero) {
        return ObscuredGlassConfig._instance.ID;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered (IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        int i1 = par1IBlockAccess.getBlockId(par2, par3, par4);
        return i1 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }
    
    @Override
    public int getRenderBlockPass() {
        return 1;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

}
