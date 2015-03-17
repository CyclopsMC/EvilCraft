package evilcraft.core.config.configurable;

import evilcraft.core.client.render.block.CustomRenderBlocks;
import evilcraft.core.client.render.block.IMultiRenderPassBlock;
import evilcraft.core.client.render.block.MultiPassBlockRenderer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlock(ExtendedConfig eConfig, Material material) {
        super(material);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    private void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }
    
    /*@Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(EnumFacing side, IBlockState blockState, int renderPass) {
        if(renderPass < 0) return null;
        return super.getIcon(side, meta);
    }*/
    
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
    public void updateTileEntity(IBlockAccess world, BlockPos blockPos) {
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
