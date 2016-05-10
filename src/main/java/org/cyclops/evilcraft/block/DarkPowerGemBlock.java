package org.cyclops.evilcraft.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockConnectedTexture;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.util.Random;

/**
 * Storage blockState for the dark power gem.
 * @author rubensworks
 *
 */
public class DarkPowerGemBlock extends ConfigurableBlockConnectedTexture {

    private static DarkPowerGemBlock _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkPowerGemBlock getInstance() {
        return _instance;
    }

    public DarkPowerGemBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos blockPos, BlockPos beacon) {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }

}
