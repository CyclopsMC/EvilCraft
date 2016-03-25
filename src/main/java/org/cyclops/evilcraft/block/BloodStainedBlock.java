package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.UnlistedProperty;
import org.cyclops.cyclopscore.client.icon.Icon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.client.particle.EntityBloodSplashFX;
import org.cyclops.evilcraft.client.render.model.ModelBloodStainedBlock;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import org.cyclops.evilcraft.tileentity.TileBloodStainedBlock;

/**
 * Multiple blockState types (defined by metadata) that have blood stains.
 * @author rubensworks
 *
 */
public class BloodStainedBlock extends ConfigurableBlockWithInnerBlocksExtended {

    @BlockProperty
    public static final IUnlistedProperty<IBlockState> INNERBLOCK = new UnlistedProperty<IBlockState>("innerblock", IBlockState.class);
    @BlockProperty
    public static final IUnlistedProperty<BlockPos> POS = new UnlistedProperty<BlockPos>("pos", BlockPos.class);

    @Icon(location = "blocks/bloodStainedBlock_0")
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon0;
    @Icon(location = "blocks/bloodStainedBlock_1")
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon1;
    @Icon(location = "blocks/bloodStainedBlock_2")
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite icon2;

    private static BloodStainedBlock _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodStainedBlock getInstance() {
        return _instance;
    }

    public BloodStainedBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.clay, TileBloodStainedBlock.class);
        this.setHardness(0.5F);

        if(MinecraftHelpers.isClientSide()) {
            eConfig.getMod().getIconProvider().registerIconHolderObject(this);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void onBlockClicked(World par1World, BlockPos blockPos, EntityPlayer par5EntityPlayer) {
        splash(par1World, blockPos);
        super.onBlockClicked(par1World, blockPos, par5EntityPlayer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState blockState, Entity entity) {
        splash(world, blockPos);
        super.onEntityCollidedWithBlock(world, blockPos, blockState, entity);
    }
    
    /**
     * Spawn particles.
     * @param world The world.
     * @param blockPos The position.
     */
    @SideOnly(Side.CLIENT)
    public static void splash(World world, BlockPos blockPos) {
    	if(MinecraftHelpers.isClientSide()) {
    		EntityBloodSplashFX.spawnParticles(world, blockPos.add(0, 1, 0), 0, 1 + world.rand.nextInt(3));
    	}
    }
    
    @Override
    public void fillWithRain(World world, BlockPos blockPos) {
        unwrapInnerBlock(world, blockPos);
    }
    
    /**
     * Stain a blockState, or add blood to the already stained blockState.
     * @param world The world.
     * @param blockPos The position.
     * @param amount The amount of blood to add.
     */
    public void stainBlock(World world, BlockPos blockPos, int amount) {
    	if(world.getBlockState(blockPos).getBlock() != this) {
    		setInnerBlock(world, blockPos);
    	}
    	TileBloodStainedBlock tile = (TileBloodStainedBlock) world.getTileEntity(blockPos);
		tile.addAmount(amount);
    }
    
    /**
     * Drain a given amount of blood from a stained blockState.
     * @param world The world.
     * @param blockPos The position.
     * @param amount The amount of blood to drain.
     * @return The unstain result with a blockState and amount.
     */
    public UnstainResult unstainBlock(World world, BlockPos blockPos, int amount) {
    	UnstainResult result = new UnstainResult();
    	if(world.getBlockState(blockPos).getBlock() == this) {
    		TileBloodStainedBlock tile = (TileBloodStainedBlock) world.getTileEntity(blockPos);
    		int foundAmount = tile.getAmount();
    		result.amount = Math.min(amount, foundAmount);
    		tile.addAmount(-result.amount);
    		if(result.amount > 0) {
    			result.block = unwrapInnerBlock(world, blockPos);
    		}
    	}
    	return result;
    }
    
    protected boolean isBlacklisted(Block block) {
    	String name = Block.blockRegistry.getNameForObject(block).toString();
        for(String blacklisted : BloodStainedBlockConfig.blockBlacklist) {
    		if(blacklisted.equals(name)) return true;
    	}
    	return false;
    }
    
    @Override
    public boolean canSetInnerBlock(IBlockState blockState, Block block, IBlockAccess world, BlockPos blockPos) {
    	return super.canSetInnerBlock(blockState, block, world, blockPos) && !isBlacklisted(block);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) super.getExtendedState(state, world, pos);
        TileBloodStainedBlock tile = TileHelpers.getSafeTile(world, pos, TileBloodStainedBlock.class);
        if(tile != null) {
            extendedBlockState = extendedBlockState.withProperty(INNERBLOCK, tile.getInnerBlockState());
        } else {
            extendedBlockState = extendedBlockState.withProperty(INNERBLOCK, Blocks.stone.getDefaultState());
        }
        extendedBlockState = extendedBlockState.withProperty(POS, pos);
        return extendedBlockState;
    }

    @Override
    public boolean hasDynamicModel() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IBakedModel createDynamicModel() {
        return new ModelBloodStainedBlock();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    /**
     * A result from unstaining a blockState.
     * @author rubensworks
     */
    public static class UnstainResult {
    	
    	/**
    	 * The amount that was drained.
    	 */
    	public int amount = 0;
    	/**
    	 * The unwrapped inner blockState.
    	 */
    	public IBlockState block = null;
    	
    }

}
