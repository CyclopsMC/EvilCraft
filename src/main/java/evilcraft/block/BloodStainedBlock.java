package evilcraft.block;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.client.particle.EntityBloodSplashFX;
import evilcraft.core.client.render.block.AlternatingBlockIconComponent;
import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.core.helper.RenderHelpers;
import evilcraft.tileentity.TileBloodStainedBlock;

/**
 * Multiple block types (defined by metadata) that have blood stains.
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        alternatingBlockIconComponent.registerIcons(getTextureName(), iconRegister);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return this.getIcon(side, world.getBlockMetadata(x, y, z), pass,
        		alternatingBlockIconComponent.getAlternateIcon(world, x, y, z, side), getTile(world, x, y, z).getInnerBlock());
    }
    
    /**
     * Get the icon.
     * @param side The side to render.
     * @param meta The metadata for the block to render.
     * @param renderPass The renderpass.
     * @param defaultIcon The default icon to render if none needs to be rendered.
     * @param baseBlock The base block.
     * @return The icon.
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta, int renderPass, IIcon defaultIcon, Block baseBlock) {
        if(renderPass < 0) {
            return RenderHelpers.EMPTYICON;
        } else if(renderPass == 1) {
            if(side != ForgeDirection.UP.ordinal())
                return RenderHelpers.EMPTYICON;
            return defaultIcon;
        } else {
            return baseBlock.getIcon(side, meta);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
        splash(par1World, par2, par3, par4);
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
        splash(par1World, par2, par3, par4);
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }
    
    /**
     * Spawn particles.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public static void splash(World world, int x, int y, int z) {
        EntityBloodSplashFX.spawnParticles(world, x, y + 1, z, 1, 1 + world.rand.nextInt(3));
    }
    
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
        // Transform to regular block when it rains
        world.setBlock(x, y, z, getTile(world, x, y, z).getInnerBlock());
        world.removeTileEntity(x, y, z);
    }
    
    /**
     * Stain a block, or add blood to the already stained block.
     * @param world The world.
     * @param location The location.
     * @param amount The amount of blood to add.
     */
    public void stainBlock(World world, ILocation location, int amount) {
    	if(LocationHelpers.getBlock(world, location) != this) {
    		setInnerBlock(world, location);
    	}
    	TileBloodStainedBlock tile = (TileBloodStainedBlock) LocationHelpers.getTile(world, location);
		tile.addAmount(amount);
    }
    
    /**
     * Drain a given amount of blood from a stained block.
     * @param world The world.
     * @param location The location.
     * @param amount The amount of blood to drain.
     * @return The unstain result with a block and amount.
     */
    public UnstainResult unstainBlock(World world, ILocation location, int amount) {
    	UnstainResult result = new UnstainResult();
    	if(LocationHelpers.getBlock(world, location) == this) {
    		TileBloodStainedBlock tile = (TileBloodStainedBlock) LocationHelpers.getTile(world, location);
    		int foundAmount = tile.getAmount();
    		result.amount = Math.min(amount, foundAmount);
    		tile.addAmount(-result.amount);
    		if(amount >= foundAmount) {
    			result.block = unwrapInnerBlock(world, location);
    		}
    	}
    	return result;
    }
    
    protected boolean isBlacklisted(Block block) {
    	String name = Block.blockRegistry.getNameForObject(block);
    	for(String blacklisted : BloodStainedBlockConfig.blockBlacklist) {
    		return blacklisted.equals(name);
    	}
    	return false;
    }
    
    @Override
    public boolean canSetInnerBlock(Block block, IBlockAccess world, int x, int y, int z) {
    	return super.canSetInnerBlock(block, world, x, y, z) && !isBlacklisted(block);
    }
    
    /**
     * A result from unstaining a block.
     * @author rubensworks
     */
    public static class UnstainResult {
    	
    	/**
    	 * The amount that was drained.
    	 */
    	public int amount = 0;
    	/**
    	 * The unwrapped inner block.
    	 */
    	public Block block = null;
    	
    }

}
