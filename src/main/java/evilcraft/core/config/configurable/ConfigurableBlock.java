package evilcraft.core.config.configurable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.client.render.block.CustomRenderBlocks;
import evilcraft.core.client.render.block.IMultiRenderPassBlock;
import evilcraft.core.client.render.block.MultiPassBlockRenderer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlock extends Block implements IConfigurable, IMultiRenderPassBlock{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    protected int pass = 0;
    protected CustomRenderBlocks renderer;
    protected boolean isInventoryBlock = false;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlock(ExtendedConfig eConfig, Material material) {
        super(material);
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return getIcon(side, meta, pass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta, int renderPass) {
        if(renderPass < 0) return null;
        return super.getIcon(side, meta);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return getRenderPasses() == 1? super.getRenderType() : MultiPassBlockRenderer.ID;
    }
    
    @Override
    public int getRenderPasses() {
        return 1;
    }
    
    @Override
    public void setRenderPass(int pass) {
        if(pass < getRenderPasses())
            this.pass = pass;
        else
            this.pass = getRenderPasses() - 1;
    }
    
    @Override
    public boolean shouldRender(int pass) {
    	return true;
    }
    
    @Override
    public void setRenderBlocks(CustomRenderBlocks renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public CustomRenderBlocks getRenderBlocks() {
        return this.renderer;
    }
    
    @Override
    public void updateTileEntity(IBlockAccess world, int x, int y, int z) {
        // There was absolutely nothing here...
    }
    
    @Override
    public void setInventoryBlock(boolean isInventoryBlock) {
        this.isInventoryBlock = isInventoryBlock;
    }

    public ConfigurableBlock setHarvestLevelDefined(String toolClass, int level) {
        super.setHarvestLevel(toolClass, level);
        return this;
    }

}
