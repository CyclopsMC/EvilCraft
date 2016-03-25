package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
		this.setStepSound(SoundType.CLOTH);
		this.setHardness(50.0F);
		this.setResistance(6000000.0F);   // Can not be destroyed by explosions
        this.setLightLevel(0.5F);
        this.setRotatable(false);
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState worldIn, World pos, BlockPos state) {
        return new AxisAlignedBB(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState blockState) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }
	
	@Override
	public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockStatedata, EntityPlayer player) {
	    // Portals should not drop upon breaking
	    world.setBlockToAir(blockPos);
	}

    protected static boolean canReplaceBlock(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return blockState != null && (blockState.getBlock().isAir(blockState, world, pos)|| blockState.getMaterial().isReplaceable());
    }

    public static boolean tryPlacePortal(World world, BlockPos blockPos) {
        int attempts = 9;
        for(RegionIterator it = new RegionIterator(blockPos, 1, true); it.hasNext() && attempts >= 0;) {
            BlockPos location = it.next();
            if(canReplaceBlock(world.getBlockState(location), world, blockPos)) {
                world.setBlockState(location, SpiritPortal.getInstance().getDefaultState(),
                        MinecraftHelpers.BLOCK_NOTIFY | MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                return true;
            }
            attempts--;
        }
        return false;
    }
}
