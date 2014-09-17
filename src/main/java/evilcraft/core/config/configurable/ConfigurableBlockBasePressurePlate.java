package evilcraft.core.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ExtendedConfig;

/**
 * {@link BlockBasePressurePlate} that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockBasePressurePlate extends BlockBasePressurePlate implements IConfigurable{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockBasePressurePlate(ExtendedConfig eConfig, Material material) {
        super("", material);
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
        setHardness(2F);
        setStepSound(Block.soundTypeStone);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if(!canPlaceBlockAt(world, x, y, z)) {
        	this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }
    
    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(getTextureName());
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setMetaBlockBounds(world, x, y, z, world.getBlockMetadata(x, y, z));
    }

    protected void setMetaBlockBounds(IBlockAccess world, int x, int y, int z, int meta) {
        super.func_150063_b(meta);
    }

}
