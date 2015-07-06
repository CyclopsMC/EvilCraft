package evilcraft.block;

import evilcraft.client.particle.EntityBloodSplashFX;
import evilcraft.core.client.render.block.AlternatingBlockIconComponent;
import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.tileentity.TileBloodStainedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Multiple blockState types (defined by metadata) that have blood stains.
 * @author rubensworks
 *
 */
public class BloodStainedBlock extends ConfigurableBlockWithInnerBlocksExtended {
    
    private static BloodStainedBlock _instance = null;
    private AlternatingBlockIconComponent alternatingBlockIconComponent = new AlternatingBlockIconComponent(getAlternateIconsAmount());
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new BloodStainedBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodStainedBlock getInstance() {
        return _instance;
    }

    private BloodStainedBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.sponge, TileBloodStainedBlock.class);
        this.setHardness(0.5F);
        this.setStepSound(soundTypeGravel);
    }
    
    /**
     * Get the amount of alternative icons for the blood stains.
     * @return The amount of icons.
     */
    public int getAlternateIconsAmount() {
        return 3;
    }
    
    @Override
    public int getRenderPasses() {
        return 2;
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
    		if(amount >= foundAmount) {
    			result.block = unwrapInnerBlock(world, blockPos);
    		}
    	}
    	return result;
    }
    
    protected boolean isBlacklisted(Block block) {
    	String name = (String) Block.blockRegistry.getNameForObject(block);
    	for(String blacklisted : BloodStainedBlockConfig.blockBlacklist) {
    		if(blacklisted.equals(name)) return true;
    	}
    	return false;
    }
    
    @Override
    public boolean canSetInnerBlock(Block block, IBlockAccess world, BlockPos blockPos) {
    	return super.canSetInnerBlock(block, world, blockPos) && !isBlacklisted(block);
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
