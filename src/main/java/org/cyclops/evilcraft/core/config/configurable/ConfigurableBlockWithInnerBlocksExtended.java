package org.cyclops.evilcraft.core.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.core.tileentity.InnerBlocksTileEntity;

import javax.annotation.Nullable;
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
    public static InnerBlocksTileEntity getTile(IBlockAccess world, BlockPos blockPos) throws InvalidInnerBlocksTileException {
    	TileEntity tile = world.getTileEntity(blockPos);
    	if(tile == null || !(tile instanceof InnerBlocksTileEntity)) {
    		throw new InvalidInnerBlocksTileException();
    	}
    	return (InnerBlocksTileEntity) tile;
    }
    
    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos blockPos, EntityPlayer player, boolean willHarvest) {
    	unwrapInnerBlock(world, blockPos);
    	return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos blockPos, EntityPlayer player) {
        try {
            return getTile(world, blockPos).getInnerBlockState().getBlock().canHarvestBlock(world, blockPos, player);
        } catch (InvalidInnerBlocksTileException e) {
            return Blocks.stone.canHarvestBlock(world, blockPos, player);
        } catch (IllegalArgumentException e) {
            return Blocks.stone.canHarvestBlock(world, blockPos, player);
        }
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos blockPos) {
        try {
            return getTile(world, blockPos).getInnerBlockState().getBlock().getBlockHardness(blockState, world, blockPos);
        } catch (InvalidInnerBlocksTileException e) {
            return Blocks.stone.getBlockHardness(blockState, world, blockPos);
        } catch (IllegalArgumentException e) {
            return Blocks.stone.getBlockHardness(blockState, world, blockPos);
        }
    }

    @Override
    public float getPlayerRelativeBlockHardness(IBlockState blockState, EntityPlayer player, World world, BlockPos blockPos) {
        try {
            return getTile(world, blockPos).getInnerBlockState().getBlock().getPlayerRelativeBlockHardness(blockState, player, world, blockPos);
        } catch (InvalidInnerBlocksTileException e) {
            return Blocks.stone.getPlayerRelativeBlockHardness(blockState, player, world, blockPos);
        } catch (IllegalArgumentException e) {
            return Blocks.stone.getPlayerRelativeBlockHardness(blockState, player, world, blockPos);
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos blockPos, EntityPlayer player) {
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
        IBlockState blockState = world.getBlockState(blockPos);
    	Block block = blockState.getBlock();
    	if(canSetInnerBlock(blockState, block, world, blockPos)) {
            IBlockState state = world.getBlockState(blockPos);
            world.setBlockState(blockPos, getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            try {
                getTile(world, blockPos).setInnerBlockState(state);
            } catch (InvalidInnerBlocksTileException e) {
                e.printStackTrace();
            }
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
    	world.setBlockState(blockPos, block, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    	return state;
    }
    
    /**
     * If the given blockState can be set to an inner blockState of this.
     * @param block The blockState to set as inner blockState.
     * @param world The world.
     * @param blockPos The position.
     * @return If the blockState can be set as inner blockState.
     */
    public boolean canSetInnerBlock(IBlockState blockState, Block block, IBlockAccess world, BlockPos blockPos) {
    	return block != null
    			&& !block.isAir(blockState, world, blockPos)
    			&& block.isOpaqueCube(blockState)
    			&& !block.hasTileEntity(world.getBlockState(blockPos))
    			&& block.getRenderType(blockState) == EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState blockStateOuter, World worldObj, RayTraceResult target, EffectRenderer effectRenderer) {
        IBlockState blockState;
        try {
            blockState = getTile(worldObj, target.getBlockPos()).getInnerBlockState();
        } catch (InvalidInnerBlocksTileException e) {
            blockState =  Blocks.stone.getDefaultState();
        }
        BlockPos pos = target.getBlockPos();
        RenderHelpers.addBlockHitEffects(effectRenderer, worldObj, blockState, pos, target.sideHit);
        return true;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return new BlockColor();
    }

    @SideOnly(Side.CLIENT)
    public static class BlockColor implements IBlockColor {
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState blockState, IBlockAccess world, BlockPos blockPos, int renderPass) {
            try {
                IBlockState innerBlockState = getTile(world, blockPos).getInnerBlockState();
                return Minecraft.getMinecraft().getBlockColors().colorMultiplier(innerBlockState, world, blockPos, renderPass);
            } catch (InvalidInnerBlocksTileException e) {
                return -1;
            } catch (NullPointerException e) {
                return -1;
            } catch (IllegalArgumentException e) {
                return -1;
            }
        }
    }

    /**
     * Exception that can occur when the tile is invalid.
     * @author rubensworks
     */
    public static class InvalidInnerBlocksTileException extends Exception {
    	
    }
    
}
