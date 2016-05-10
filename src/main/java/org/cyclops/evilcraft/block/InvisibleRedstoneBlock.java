package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.item.RedstoneGrenade;
import org.cyclops.evilcraft.item.RedstoneGrenadeConfig;
import org.cyclops.evilcraft.tileentity.TileInvisibleRedstoneBlock;

import java.util.List;
import java.util.Random;

/**
 * An invisible blockState where players can walk through and disappears after a few ticks.
 * @author immortaleeb
 *
 */
public class InvisibleRedstoneBlock extends ConfigurableBlockContainer {

    private static InvisibleRedstoneBlock _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static InvisibleRedstoneBlock getInstance() {
        return _instance;
    }

    public InvisibleRedstoneBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.air, TileInvisibleRedstoneBlock.class);
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(SoundType.METAL);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
    	if(Configs.isEnabled(RedstoneGrenadeConfig.class)) {
    		return RedstoneGrenade.getInstance();
    	} else {
    		return null;
    	}
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing side) {
        return 15;
    }
    
    @Override
    public boolean canProvidePower(IBlockState blockState) {
        return true;
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState blockState) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos blockPos) {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World world, BlockPos blockPos) {
        return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World world, BlockPos blockPos) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState blockStatedata) {
        return new TileInvisibleRedstoneBlock(world);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        // Do not appear in creative tab
    }
}
