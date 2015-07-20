package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.algorithm.RegionIterator;
import org.cyclops.evilcraft.tileentity.TileSpiritPortal;

/**
 * Portal for throwing in your book and stuff.
 * @author rubensworks
 *
 */
public class SpiritPortal extends ConfigurableBlockContainer {

	private static SpiritPortal _instance = null;

	/**
     * Get the unique instance.
     * @return The instance.
     */
    public static SpiritPortal getInstance() {
        return _instance;
    }

	public SpiritPortal(ExtendedConfig<BlockConfig> eConfig) {
		super(eConfig, Material.iron, TileSpiritPortal.class);
		this.setRotatable(true);
		this.setStepSound(soundTypeCloth);
		this.setHardness(50.0F);
		this.setResistance(6000000.0F);   // Can not be destroyed by explosions
        this.setBlockBounds(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
        this.setLightLevel(0.5F);
        this.setRotatable(false);
	}

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }
	
	@Override
	public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockStatedata, EntityPlayer player) {
	    // Portals should not drop upon breaking
	    world.setBlockToAir(blockPos);
	}

    protected static boolean canReplaceBlock(Block block) {
        return block != null && (block == Blocks.air || block.getMaterial().isReplaceable());
    }

    public static boolean tryPlacePortal(World world, BlockPos blockPos) {
        int attempts = 9;
        for(RegionIterator it = new RegionIterator(blockPos, 1, true); it.hasNext() && attempts >= 0;) {
            BlockPos location = it.next();
            if(canReplaceBlock(world.getBlockState(location).getBlock())) {
                world.setBlockState(location, SpiritPortal.getInstance().getDefaultState(),
                        MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                return true;
            }
            attempts--;
        }
        return false;
    }
}
