package evilcraft.api.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.DirectionCorner;
import evilcraft.api.Helpers;
import evilcraft.api.RenderHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.TileConnectedTexture;
import evilcraft.api.render.ConnectableIcon;
import evilcraft.api.render.CustomRenderBlocks;
import evilcraft.api.render.MultiPassBlockRenderer;

public abstract class ConfigurableBlockConnectedTexture extends ConfigurableBlockContainer implements IMultiRenderPassBlock{
    
    protected int pass = 0;
    protected CustomRenderBlocks renderer;
    
    protected ConnectableIcon connectableIcon;
    protected Icon blockIconBorder;
    protected Icon blockIconCorner;
    protected Icon blockIconInnerCorner;
    protected Icon blockIconInventory;

    public ConfigurableBlockConnectedTexture(ExtendedConfig eConfig,
            Material material) {
        super(eConfig, material, TileConnectedTexture.class);
    }
    
    public Icon getIconBackground() {
        return blockIcon;
    }

    public Icon getIconBorders() {
        return blockIconBorder;
    }

    public Icon getIconCorners() {
        return blockIconCorner;
    }

    public Icon getIconInnerCorners() {
        return blockIconInnerCorner;
    }
    
    public Icon getIconInventory() {
        return hasSeperateInventoryBlockIcon()?blockIconInventory:blockIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return MultiPassBlockRenderer.ID;
    }
    
    public boolean shouldConnect(IBlockAccess world, int x, int y, int z) {
        return true;
    }
    
    public boolean shouldConnectDirection(IBlockAccess world, ForgeDirection side, int x, int y, int z) {
        if(!shouldConnect(world, x, y, z))return false;
        return world.getBlockId(x + side.offsetX, y + side.offsetY, z + side.offsetZ) == this.blockID;
    }
    
    public boolean shouldConnectDirection(IBlockAccess world, DirectionCorner side, int x, int y, int z) {
        if(!shouldConnect(world, x, y, z))return false;
        return world.getBlockId(x + side.offsetX, y + side.offsetY, z + side.offsetZ) == this.blockID;
    }
    
    @Override
    public void setRenderPass(int pass) {
        if(pass < getRenderPasses())
            this.pass = pass;
        else
            this.pass = getRenderPasses() - 1;
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
        TileEntity tile = (TileEntity) world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileConnectedTexture){
            TileConnectedTexture tileConnectedTexture = (TileConnectedTexture) tile;
            connectableIcon.setTileConnectedTexture(tileConnectedTexture);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        super.registerIcons(iconRegister);
        blockIconBorder = iconRegister.registerIcon(getTextureName()+"_border");
        blockIconCorner = iconRegister.registerIcon(getTextureName()+"_corner");
        blockIconInnerCorner = iconRegister.registerIcon(getTextureName()+"_innerCorner");
        if(hasSeperateInventoryBlockIcon())
            blockIconInventory = iconRegister.registerIcon(getTextureName()+"_inventory");
        connectableIcon = new ConnectableIcon(getIconBackground(), getIconBorders(), getIconCorners(), getIconInnerCorners(), getIconInventory());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        return getIcon(side, meta, pass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta, int renderPass) {
        if(this.getRenderBlocks() != null) { // In case for inventoryblock?
            connectableIcon.prepareIcon(side, renderPass, this.getRenderBlocks());
        }
        return connectableIcon;
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        updateConnections(world, x, y, z);// If there are framerate issues, this will propably be the cause.
        return super.getBlockTexture(world, x, y, z, side);
    }
    
    @Override
    public int getRenderPasses() {
        return connectableIcon.getRequiredPasses();
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int meta){
        updateConnections(world, x, y, z);
    }
    
    private void updateConnections(IBlockAccess world, int x, int y, int z) {
        TileEntity tile = (TileEntity) world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileConnectedTexture){
            TileConnectedTexture tileConnectedTexture = (TileConnectedTexture) tile;
            // Regular sides
            for(ForgeDirection direction : Helpers.DIRECTIONS) {
                boolean connect = shouldConnectDirection(world, direction, x, y, z);
                tileConnectedTexture.connect(direction, connect);
            }
            // Corner sides
            for(DirectionCorner direction : Helpers.DIRECTIONS_CORNERS) {
                boolean connect = shouldConnectDirection(world, direction, x, y, z);
                tileConnectedTexture.connectCorner(direction, connect);
            }
        }
    }
    
    public void setInventoryBlock(boolean isInventoryBlock) {
        this.connectableIcon.setInventoryBlock(isInventoryBlock);
    }
    
    /**
     * If this block has a seperate icon for rendering the inventory block, false by default.
     * If it is true, the renderer will look for getTextureName()+"_inventory" icon, otherwise
     * the background of the block will be taken (without corners, edges and innercorners).
     * @return If this block has a seperate icon for rendering the inventory block.
     */
    public boolean hasSeperateInventoryBlockIcon() {
        return false;
    }

}
