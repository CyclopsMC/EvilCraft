package evilcraft.core.config.configurable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.tileentity.InnerBlocksTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * A block that is based on inner blocks that are stored in a tile entity.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockWithInnerBlocksExtended extends ConfigurableBlockContainer {
    
    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     * @param material Material of this block.
     * @param tileEntity The tile class
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableBlockWithInnerBlocksExtended(ExtendedConfig eConfig, Material material, Class<? extends InnerBlocksTileEntity> tileEntity) {
        super(eConfig, material, tileEntity);
        setRotatable(false);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        // Do not appear in creative tab
    }
    
    @Override
    public boolean saveNBTToDroppedItem() {
        return false;
    }
    
    /**
     * Get the tile entity.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @return The tile.
     * @throws InvalidInnerBlocksTileException If the found tile was invalid or no {@link InnerBlocksTileEntity}.
     */
    public InnerBlocksTileEntity getTile(IBlockAccess world, int x, int y, int z) throws InvalidInnerBlocksTileException {
    	TileEntity tile = world.getTileEntity(x, y, z);
    	if(tile == null || !(tile instanceof InnerBlocksTileEntity)) {
    		throw new InvalidInnerBlocksTileException();
    	}
    	return (InnerBlocksTileEntity) tile;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    	try {
			return getTile(world, x, y, z).getInnerBlock().getIcon(side, world.getBlockMetadata(x, y, z));
		} catch (InvalidInnerBlocksTileException e) {
			return Blocks.stone.getIcon(world, x, y, z, side);
		} catch (NullPointerException e) {
            return Blocks.stone.getIcon(world, x, y, z, side);
        }
    }
    
    @Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
    	unwrapInnerBlock(world, x, y, z);
    	return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        try {
			return getTile(world, x, y, z).getInnerBlock().colorMultiplier(world, x, y, z);
		} catch (InvalidInnerBlocksTileException e) {
			return Blocks.stone.colorMultiplier(world, x, y, z);
		} catch (NullPointerException e) {
            return Blocks.stone.colorMultiplier(world, x, y, z);
        }
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    	Block block;
		try {
			block = getTile(world, x, y, z).getInnerBlock();
            if(block == null) block = Blocks.stone;
		} catch (InvalidInnerBlocksTileException e) {
			block = Blocks.stone;
		}
    	return new ItemStack(block, 1, block.getDamageValue(world, x, y, z));
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        try {
            return getTile(world, x, y, z).getInnerBlock().getBlockHardness(world, x, y, z);
        } catch (InvalidInnerBlocksTileException e) {
            return Blocks.stone.getBlockHardness(world, x, y, z);
        } catch (NullPointerException e) {
            return Blocks.stone.getBlockHardness(world, x, y, z);
        }
    }
    
    /**
     * Convert the block at the given location to an inner block of this.
     * @param world The world.
     * @param location The location.
     * @return If the block could be set as inner block.
     */
    public boolean setInnerBlock(World world, ILocation location) {
    	int[] c = location.getCoordinates();
    	return setInnerBlock(world, c[0], c[1], c[2]);
    }
    
    /**
     * Convert the block at the given location to an inner block of this.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @return If the block could be set as inner block.
     */
    public boolean setInnerBlock(World world, int x, int y, int z) {
    	Block block = world.getBlock(x, y, z);
    	if(canSetInnerBlock(block, world, x, y, z)) {
    		int meta = world.getBlockMetadata(x, y, z);
    		world.setBlock(x, y, z, this);
    		try {
				getTile(world, x, y, z).setInnerBlock(block);
			} catch (InvalidInnerBlocksTileException e) {
				e.printStackTrace();
			}
    		world.setBlockMetadataWithNotify(x, y, z, meta, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    		return true;
    	}
    	return false;
    }
    
    /**
     * Unwrap the inner block from this block.
     * @param world The world.
     * @param location The location.
     * @return The newly set block.
     */
    public Block unwrapInnerBlock(World world, ILocation location) {
    	int[] c = location.getCoordinates();
    	return unwrapInnerBlock(world, c[0], c[1], c[2]);
    }
    
    /**
     * Try to unwrap the inner block from this block.
     * Will return null if failed.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @return The newly set block or null.
     */
    public Block unwrapInnerBlock(World world, int x, int y, int z) {
    	InnerBlocksTileEntity tile = null;
		try {
			tile = getTile(world, x, y, z);
		} catch (InvalidInnerBlocksTileException e) {
			e.printStackTrace();
		}
    	if(tile == null) return null;
    	Block block = tile.getInnerBlock();
    	if(block == null) return null;
    	int meta = world.getBlockMetadata(x, y, z);
    	world.setBlock(x, y, z, block);
    	world.setBlockMetadataWithNotify(x, y, z, meta, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    	return block;
    }
    
    /**
     * If the given block can be set to an inner block of this.
     * @param block The block to set as inner block.
     * @param world The world.
     * @param x X
     * @param y Y
     * @param z Z
     * @return If the block can be set as inner block.
     */
    public boolean canSetInnerBlock(Block block, IBlockAccess world, int x, int y, int z) {
    	return block != null
    			&& !block.isAir(world, x, y, z)
    			&& block.isOpaqueCube()
    			&& !block.hasTileEntity(world.getBlockMetadata(x, y, z))
    			&& block.getRenderType() == 0;
    }
    
    /**
     * Exception that can occur when the tile is invalid.
     * @author rubensworks
     */
    public static class InvalidInnerBlocksTileException extends Exception {
    	
    }
    
}
