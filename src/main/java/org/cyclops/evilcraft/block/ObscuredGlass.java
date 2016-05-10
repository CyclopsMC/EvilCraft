package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockConnectedTexture;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Random;

/**
 * Glass that holds back some light.
 * @author rubensworks
 *
 */
public class ObscuredGlass extends ConfigurableBlockConnectedTexture {
    
    private static ObscuredGlass _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ObscuredGlass getInstance() {
        return _instance;
    }

    public ObscuredGlass(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.glass);
        this.setHardness(0.5F);
        this.setStepSound(SoundType.GLASS);
        this.setLightOpacity(10);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos blockPos, EnumFacing side) {
        Block block = world.getBlockState(blockPos).getBlock();
        return block != this && super.shouldSideBeRendered(blockState, world, blockPos, side);
    }
    
    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
