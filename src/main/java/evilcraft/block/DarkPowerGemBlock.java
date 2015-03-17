package evilcraft.block;

import evilcraft.core.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

/**
 * Storage blockState for the dark power gem.
 * @author rubensworks
 *
 */
public class DarkPowerGemBlock extends ConfigurableBlockConnectedTexture {

    private static DarkPowerGemBlock _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkPowerGemBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkPowerGemBlock getInstance() {
        return _instance;
    }

    private DarkPowerGemBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeMetal);
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

    @Override
    public boolean hasSeperateInventoryBlockIcon() {
        return true;
    }

}
