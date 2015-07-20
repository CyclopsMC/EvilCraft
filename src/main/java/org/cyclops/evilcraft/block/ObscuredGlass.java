package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
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
        this.setStepSound(soundTypeGlass);
        this.setLightOpacity(10);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos blockPos, EnumFacing side) {
        Block block = world.getBlockState(blockPos).getBlock();
        return block == this ? false : super.shouldSideBeRendered(world, blockPos, side);
    }
    
    @Override
    public boolean isNormalCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
}
