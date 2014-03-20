package evilcraft.api.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.render.CustomRenderBlocks;
import evilcraft.api.render.IMultiRenderPassBlock;
import evilcraft.api.render.MultiPassBlockRenderer;

/**
 * Block that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlock extends Block implements Configurable, IMultiRenderPassBlock{
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.BLOCK;
    
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
        this.setBlockName(this.getUniqueName());
    }

    @Override
    public void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "blocks."+eConfig.NAMEDID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(getTextureName());
    }
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return false;
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

}
