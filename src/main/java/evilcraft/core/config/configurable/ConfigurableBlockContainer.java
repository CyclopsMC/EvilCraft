package evilcraft.core.config.configurable;

import evilcraft.Reference;
import evilcraft.core.client.render.block.CustomRenderBlocks;
import evilcraft.core.client.render.block.IMultiRenderPassBlock;
import evilcraft.core.client.render.block.MultiPassBlockRenderer;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.TileEntityNBTStorage;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Block with a tile entity that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public class ConfigurableBlockContainer extends BlockContainer implements IConfigurable, IMultiRenderPassBlock {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    protected Random random;
    private Class<? extends EvilCraftTileEntity> tileEntity;
    
    protected boolean hasGui = false;
    
    private boolean rotatable;
    
    protected int pass = 0;
    protected CustomRenderBlocks renderer;
    protected boolean isInventoryBlock = false;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     * @param tileEntity The class of the tile entity this blockState holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainer(ExtendedConfig eConfig, Material material, Class<? extends EvilCraftTileEntity> tileEntity) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        this.random = new Random();
        this.tileEntity = tileEntity;
        setHardness(5F);
        setStepSound(Block.soundTypePiston);
    }
    
    /**
     * Get the class of the tile entity this blockState holds.
     * @return The tile entity class.
     */
    public Class<? extends TileEntity> getTileEntity() {
        return this.tileEntity;
    }

    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    /**
     * If this blockState container has a corresponding GUI.
     * @return If it has a GUI.
     */
    public boolean hasGui() {
        return hasGui;
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
    public void updateTileEntity(IBlockAccess world, BlockPos blockPos) {
        // There was absolutely nothing here...
    }
    
    @Override
    public void setInventoryBlock(boolean isInventoryBlock) {
        this.isInventoryBlock = isInventoryBlock;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        try {
            EvilCraftTileEntity tile = tileEntity.newInstance();
            tile.onLoad();
            tile.setRotatable(isRotatable());
            return tile;
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
        	e2.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    /**
     * If the NBT data of this tile entity should be added to the dropped blockState.
     * @return If the NBT data should be added.
     */
    public boolean saveNBTToDroppedItem() {
        return true;
    }
    
    protected void onPreBlockDestroyed(World world, BlockPos blockPos) {
    	MinecraftHelpers.preDestroyBlock(this, world, blockPos, saveNBTToDroppedItem());
    }
    
    protected void onPostBlockDestroyed(World world, BlockPos blockPos) {
    	
    }
    
    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
    	onPreBlockDestroyed(world, blockPos);
        super.breakBlock(world, blockPos, blockState);
        onPostBlockDestroyed(world, blockPos);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
    	onPreBlockDestroyed(world, blockPos);
    	super.onBlockDestroyedByExplosion(world, blockPos, explosion);
    	onPostBlockDestroyed(world, blockPos);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entity, ItemStack stack) {
        if(entity != null) {
            EvilCraftTileEntity tile = (EvilCraftTileEntity) world.getTileEntity(blockPos);
            
            if(stack.getTagCompound() != null) {
                    stack.getTagCompound().setInteger("x", blockPos.getX());
                    stack.getTagCompound().setInteger("y", blockPos.getY());
                    stack.getTagCompound().setInteger("z", blockPos.getZ());
                    //stack.getTagCompound().setInteger("rotation", UNKNOWN.ordinal());
                    tile.readFromNBT(stack.getTagCompound());
            }
            
            /*if(tile.isRotatable()) {
                EnumFacing facing = DirectionHelpers.getEntityFacingDirection(entity);
                tile.setRotation(facing);
            }*/

            if(tile instanceof IUpdatePlayerListBox) {
                ((IUpdatePlayerListBox) tile).update();
            }
        }
        super.onBlockPlacedBy(world, blockPos, blockState, entity, stack);
    }
    
    /**
     * Write additional info about the tile into the item.
     * @param tile The tile that is being broken.
     * @param tag The tag that will be added to the dropped item.
     */
    public void writeAdditionalInfo(TileEntity tile, NBTTagCompound tag) {
    	
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockState, int fortune) {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        ItemStack itemStack = new ItemStack(getItemDropped(blockState, new Random(), fortune), 1, damageDropped(blockState));
		if(TileEntityNBTStorage.TAG != null) {
		    itemStack.setTagCompound(TileEntityNBTStorage.TAG);
		}
		drops.add(itemStack);
        
        MinecraftHelpers.postDestroyBlock(world, blockPos);
        return drops;
    }

    /**
     * If the NBT data of this blockState should be preserved in the item when it
     * is broken into an item.
     * @return If it should keep NBT data.
     */
    public boolean isKeepNBTOnDrop() {
		return true;
	}

	/**
     * If this blockState can be rotated.
     * @return Can be rotated.
     */
    public boolean isRotatable() {
        return rotatable;
    }

    /**
     * Set whether of not this container must be able to be rotated.
     * @param rotatable Can be rotated.
     */
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }
    
    /**
     * Get the texture path of the GUI.
     * @return The path of the GUI for this blockState.
     */
    public String getGuiTexture() {
        return getGuiTexture("");
    }
    
    /**
     * Get the texture path of the GUI.
     * @param suffix Suffix to add to the path.
     * @return The path of the GUI for this blockState.
     */
    public String getGuiTexture(String suffix) {
        return Reference.TEXTURE_PATH_GUI + eConfig.getNamedId() + "_gui" + suffix + ".png";
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos blockPos) {
    	Item item = getItem(world, blockPos);

        if (item == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(item, 1, getDamageValue(world, blockPos));
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof EvilCraftTileEntity && isKeepNBTOnDrop()) {
            EvilCraftTileEntity ecTile = ((EvilCraftTileEntity) tile);
            itemStack.setTagCompound(ecTile.getNBTTagCompound());
        }
        return itemStack;
    }

    @Override
    public final BlockContainerConfig getConfig() {
        return (BlockContainerConfig) this.eConfig;
    }

}
