package evilcraft.core.config.configurable;

import evilcraft.core.tileentity.InnerBlocksTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * A blockState that is based on inner blocks that are stored in a tile entity.
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockWithInnerBlocksExtended extends ConfigurableBlockContainer {
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
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
     * @param blockPos The position.
     * @return The tile.
     * @throws InvalidInnerBlocksTileException If the found tile was invalid or no {@link InnerBlocksTileEntity}.
     */
    public InnerBlocksTileEntity getTile(IBlockAccess world, BlockPos blockPos) throws InvalidInnerBlocksTileException {
    	TileEntity tile = world.getTileEntity(blockPos);
    	if(tile == null || !(tile instanceof InnerBlocksTileEntity)) {
    		throw new InvalidInnerBlocksTileException();
    	}
    	return (InnerBlocksTileEntity) tile;
    }
    
    /*@Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(IBlockAccess world, BlockPos blockPos, EnumFacing side) {
    	try {
			return getTile(world, x, y, z).getInnerBlockState().getIcon(side, world.getBlockMetadata(x, y, z));
		} catch (InvalidInnerBlocksTileException e) {
			return Blocks.stone.getIcon(world, x, y, z, side);
		} catch (NullPointerException e) {
            return Blocks.stone.getIcon(world, x, y, z, side);
        }
    }*/
    
    @Override
    public boolean removedByPlayer(World world, BlockPos blockPos, EntityPlayer player, boolean willHarvest) {
    	unwrapInnerBlock(world, blockPos);
    	return false;
    }
    
    /*@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, BlockPos blockPos) {
        try {
			return getTile(world, blockPos).getInnerBlockState().colorMultiplier(world, blockPos);
		} catch (InvalidInnerBlocksTileException e) {
			return Blocks.stone.colorMultiplier(world, blockPos);
		} catch (NullPointerException e) {
            return Blocks.stone.colorMultiplier(world, blockPos);
        }
    }*/
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos blockPos) {
    	IBlockState blockState;
		try {
            blockState = getTile(world, blockPos).getInnerBlockState();
            if(blockState == null) blockState = Blocks.stone.getDefaultState();
		} catch (InvalidInnerBlocksTileException e) {
            blockState = Blocks.stone.getDefaultState();
		}
    	return new ItemStack(blockState.getBlock(), 1, blockState.getBlock().getMetaFromState(blockState));
    }
    
    /**
     * Convert the blockState at the given location to an inner blockState of this.
     * @param world The world.
     * @param blockPos The position.
     * @return If the blockState could be set as inner blockState.
     */
    public boolean setInnerBlock(World world, BlockPos blockPos) {
    	Block block = world.getBlockState(blockPos).getBlock();
    	if(canSetInnerBlock(block, world, blockPos)) {
    		IBlockState state = world.getBlockState(blockPos);
    		try {
				getTile(world, blockPos).setInnerBlockState(state);
			} catch (InvalidInnerBlocksTileException e) {
				e.printStackTrace();
			}
    		world.setBlockState(blockPos, state, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    		return true;
    	}
    	return false;
    }
    
    /**
     * Try to unwrap the inner blockState from this blockState.
     * Will return null if failed.
     * @param world The world.
     * @param blockPos The position.
     * @return The newly set blockState state or null.
     */
    public IBlockState unwrapInnerBlock(World world, BlockPos blockPos) {
    	InnerBlocksTileEntity tile = null;
		try {
			tile = getTile(world, blockPos);
		} catch (InvalidInnerBlocksTileException e) {
			e.printStackTrace();
		}
    	if(tile == null) return null;
        IBlockState block = tile.getInnerBlockState();
    	if(block == null) return null;
    	IBlockState state = world.getBlockState(blockPos);
    	world.setBlockState(blockPos, state, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    	return state;
    }
    
    /**
     * If the given blockState can be set to an inner blockState of this.
     * @param block The blockState to set as inner blockState.
     * @param world The world.
     * @param blockPos The position.
     * @return If the blockState can be set as inner blockState.
     */
    public boolean canSetInnerBlock(Block block, IBlockAccess world, BlockPos blockPos) {
    	return block != null
    			&& !block.isAir(world, blockPos)
    			&& block.isOpaqueCube()
    			&& !block.hasTileEntity(world.getBlockState(blockPos))
    			&& block.getRenderType() == 0;
    }
    
    /**
     * Exception that can occur when the tile is invalid.
     * @author rubensworks
     */
    public static class InvalidInnerBlocksTileException extends Exception {
    	
    }
    
}
