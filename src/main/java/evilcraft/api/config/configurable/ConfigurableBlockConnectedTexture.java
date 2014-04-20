package evilcraft.api.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.DirectionCorner;
import evilcraft.api.Helpers;
import evilcraft.api.RenderHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.entities.tileentitites.TileConnectedTexture;
import evilcraft.api.render.ConnectableIcon;
import evilcraft.api.render.CustomRenderBlocks;
import evilcraft.api.render.IMultiRenderPassBlock;
import evilcraft.api.render.MultiPassBlockRenderer;

/**
 * Block that can hold ExtendedConfigs with connected textures.
 * It will automatically render connected textures when the same blocks are next to it.
 * It uses {@link ConnectableIcon} as the icon that dynamically changes it's appearance.
 * @author rubensworks
 * @see ConnectableIcon
 */
public abstract class ConfigurableBlockConnectedTexture extends ConfigurableBlockContainer implements IMultiRenderPassBlock{
    
    protected int pass = 0;
    protected CustomRenderBlocks renderer;
    
    protected ConnectableIcon connectableIcon;
    protected IIcon blockIconBorder;
    protected IIcon blockIconCorner;
    protected IIcon blockIconInnerCorner;
    protected IIcon blockIconInventory;

    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableBlockConnectedTexture(ExtendedConfig eConfig,
            Material material) {
        super(eConfig, material, TileConnectedTexture.class);
    }
    
    /**
     * Get the icon of the background.
     * @return The background icon.
     */
    public IIcon getIconBackground() {
        return blockIcon;
    }

    /**
     * Get the icon for the borders.
     * @return The border icon.
     */
    public IIcon getIconBorders() {
        return blockIconBorder;
    }

    /**
     * Get the icon for the corners.
     * @return The corner icon.
     */
    public IIcon getIconCorners() {
        return blockIconCorner;
    }

    /**
     * Get the inner corner icon.
     * @return The inner corner icon.
     */
    public IIcon getIconInnerCorners() {
        return blockIconInnerCorner;
    }
    
    /**
     * Get the inventory block icon.
     * @return The inventory block icon.
     * @see ConfigurableBlockConnectedTexture#hasSeperateInventoryBlockIcon()
     */
    public IIcon getIconInventory() {
        return hasSeperateInventoryBlockIcon()?blockIconInventory:blockIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return MultiPassBlockRenderer.ID;
    }
    
    /**
     * If the block at the given coordinates should connect to any other block.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If these should connect.
     */
    public boolean shouldConnect(IBlockAccess world, int x, int y, int z) {
        return true;
    }
    
    /**
     * If this block should connect at the given direction.
     * @param world The world.
     * @param side The direction to check the connection with.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If this block should connect at the given direction.
     * @see ForgeDirection
     */
    public boolean shouldConnectDirection(IBlockAccess world, ForgeDirection side, int x, int y, int z) {
        if(!shouldConnect(world, x, y, z)) return false;
        return world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ) == this;
    }
    
    /**
     * If this block should connect at the given direction corner.
     * @param world The world.
     * @param side The direction corner to check the connection with.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If this block should connect at the given direction.
     * @see DirectionCorner
     */
    public boolean shouldConnectDirection(IBlockAccess world, DirectionCorner side, int x, int y, int z) {
        if(!shouldConnect(world, x, y, z))return false;
        return world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ) == this;
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
    	return pass==0 || pass>=4;
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
        TileEntity tile = (TileEntity) world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileConnectedTexture){
            TileConnectedTexture tileConnectedTexture = (TileConnectedTexture) tile;
            connectableIcon.setTileConnectedTexture(tileConnectedTexture);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        blockIconBorder = iconRegister.registerIcon(getTextureName()+"_border");
        blockIconCorner = iconRegister.registerIcon(getTextureName()+"_corner");
        blockIconInnerCorner = iconRegister.registerIcon(getTextureName()+"_innerCorner");
        if(hasSeperateInventoryBlockIcon())
            blockIconInventory = iconRegister.registerIcon(getTextureName()+"_inventory");
        connectableIcon = new ConnectableIcon(getIconBackground(), getIconBorders(), getIconCorners(), getIconInnerCorners(), getIconInventory());
        
        //TMP
        RenderHelpers.EMPTYICON = iconRegister.registerIcon(Reference.MOD_ID+":empty");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return getIcon(side, meta, pass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta, int renderPass) {
        if(this.getRenderBlocks() != null) { // In case for inventoryblock?
            connectableIcon.prepareIcon(side, renderPass, this.getRenderBlocks());
        }
        return connectableIcon;
    }
    
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        updateConnections(world, x, y, z);// If there are framerate issues, this will propably be the cause.
        return super.getIcon(world, x, y, z, side);
    }
    
    @Override
    public int getRenderPasses() {
        return connectableIcon.getRequiredPasses();
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block){
        updateConnections(world, x, y, z);
    }
    
    private void updateConnections(IBlockAccess world, int x, int y, int z) {
        TileEntity tile = (TileEntity) world.getTileEntity(x, y, z);
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
    
    @Override
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
    
    @Override
    public boolean saveNBTToDroppedItem() {
        return false;
    }

}
