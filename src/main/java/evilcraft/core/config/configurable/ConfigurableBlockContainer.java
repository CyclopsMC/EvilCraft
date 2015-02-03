package evilcraft.core.config.configurable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.core.client.render.block.CustomRenderBlocks;
import evilcraft.core.client.render.block.IMultiRenderPassBlock;
import evilcraft.core.client.render.block.MultiPassBlockRenderer;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.TileEntityNBTStorage;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

/**
 * Block with a tile entity that can hold ExtendedConfigs.
 * @author rubensworks
 *
 */
public class ConfigurableBlockContainer extends BlockContainer implements IConfigurable, IMultiRenderPassBlock {
    
    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    protected Random random;
    private Class<? extends EvilCraftTileEntity> tileEntity;
    
    protected boolean hasGui = false;
    
    private boolean rotatable;
    protected IIcon[] sideIcons = new IIcon[DirectionHelpers.DIRECTIONS.size()];
    
    protected int pass = 0;
    protected CustomRenderBlocks renderer;
    protected boolean isInventoryBlock = false;
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     * @param tileEntity The class of the tile entity this block holds.
     */
    @SuppressWarnings({ "rawtypes" })
    public ConfigurableBlockContainer(ExtendedConfig eConfig, Material material, Class<? extends EvilCraftTileEntity> tileEntity) {
        super(material);
        this.setConfig(eConfig);
        this.setBlockName(eConfig.getUnlocalizedName());
        this.random = new Random();
        this.tileEntity = tileEntity;
        setHardness(5F);
        setStepSound(Block.soundTypePiston);
    }
    
    /**
     * Get the class of the tile entity this block holds.
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
     * If this block container has a corresponding GUI.
     * @return If it has a GUI.
     */
    public boolean hasGui() {
        return hasGui;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        if(isRotatable()) {
            for(ForgeDirection direction : DirectionHelpers.DIRECTIONS) {
                sideIcons[direction.ordinal()] = iconRegister.registerIcon(getTextureName() + "_" + direction.name());
            }
        } else {
            blockIcon = iconRegister.registerIcon(getTextureName());
        }
    }
    
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if(isRotatable()) {
            int meta = world.getBlockMetadata(x, y, z);
            EvilCraftTileEntity tile = (EvilCraftTileEntity) world.getTileEntity(x, y, z);
            ForgeDirection rotatedDirection = DirectionHelpers.TEXTURESIDE_ORIENTATION[tile.getRotation().ordinal()][side];
            return this.getIcon(rotatedDirection.ordinal(), meta);
        } else {
            return super.getIcon(world, x, y, z, side);
        }
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
    	return getIcon(side, meta, pass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta, int renderPass) {
        if(renderPass < 0) return null;
        if(isRotatable()) {
            return sideIcons[side];
        } else {
            return super.getIcon(side, meta);
        }
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
    
    @Override
    public String getTextureName() {
        return Reference.MOD_ID+":"+eConfig.getNamedId();
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
     * If the NBT data of this tile entity should be added to the dropped block.
     * @return If the NBT data should be added.
     */
    public boolean saveNBTToDroppedItem() {
        return true;
    }
    
    protected void onPreBlockDestroyed(World world, int x, int y, int z) {
    	MinecraftHelpers.preDestroyBlock(this, world, x, y, z, saveNBTToDroppedItem());
    }
    
    protected void onPostBlockDestroyed(World world, int x, int y, int z) {
    	
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    	onPreBlockDestroyed(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
        onPostBlockDestroyed(world, x, y, z);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
    	onPreBlockDestroyed(world, x, y, z);
    	super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
    	onPostBlockDestroyed(world, x, y, z);
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if(entity != null) {
            EvilCraftTileEntity tile = (EvilCraftTileEntity) world.getTileEntity(x, y, z);
            
            if(stack.getTagCompound() != null) {
                    stack.getTagCompound().setInteger("x", x);
                    stack.getTagCompound().setInteger("y", y);
                    stack.getTagCompound().setInteger("z", z);
                    stack.getTagCompound().setInteger("rotation", ForgeDirection.UNKNOWN.ordinal());
                    tile.readFromNBT(stack.getTagCompound());
            }
            
            if(tile.isRotatable()) {
                ForgeDirection facing = DirectionHelpers.getEntityFacingDirection(entity);
                tile.setRotation(facing);
            }
            
            tile.sendUpdate();
        }
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
    }
    
    /**
     * Write additional info about the tile into the item.
     * @param tile The tile that is being broken.
     * @param tag The tag that will be added to the dropped item.
     */
    public void writeAdditionalInfo(TileEntity tile, NBTTagCompound tag) {
    	
    }
    
    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {
        if(!world.isRemote) {
            EvilCraftTileEntity tile = (EvilCraftTileEntity) world.getTileEntity(x, y, z);
            if(tile.isRotatable()) {
                tile.setRotation(tile.getRotation().getRotation(axis));
                return true;
            }
        }
        return false;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        ItemStack itemStack = new ItemStack(getItemDropped(metadata, world.rand, fortune), 1, damageDropped(metadata));
		if(TileEntityNBTStorage.TAG != null) {
		    itemStack.setTagCompound(TileEntityNBTStorage.TAG);
		}
		drops.add(itemStack);
        
        MinecraftHelpers.postDestroyBlock(world, x, y, z);
        return drops;
    }

    /**
     * If the NBT data of this block should be preserved in the item when it
     * is broken into an item.
     * @return If it should keep NBT data.
     */
    public boolean isKeepNBTOnDrop() {
		return true;
	}

	/**
     * If this block can be rotated.
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
     * @return The path of the GUI for this block.
     */
    public String getGuiTexture() {
        return getGuiTexture("");
    }
    
    /**
     * Get the texture path of the GUI.
     * @param suffix Suffix to add to the path.
     * @return The path of the GUI for this block.
     */
    public String getGuiTexture(String suffix) {
        return Reference.TEXTURE_PATH_GUI + eConfig.getNamedId() + "_gui" + suffix + ".png";
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    	Item item = getItem(world, x, y, z);

        if (item == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(item, 1, getDamageValue(world, x, y, z));
        TileEntity tile = world.getTileEntity(x, y, z);
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
