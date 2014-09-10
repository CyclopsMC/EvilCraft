package evilcraft.block;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import evilcraft.core.config.configurable.ConfigurableBlockConnectedTexture;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.item.DarkGem;

/**
 * A storage block for {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkBlock extends ConfigurableBlockConnectedTexture {
    
    private static DarkBlock _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new DarkBlock(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkBlock getInstance() {
        return _instance;
    }

    private DarkBlock(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.rock);
        this.setHardness(5.0F);
        this.setStepSound(soundTypeMetal);
        this.setHarvestLevel("pickaxe", 2); // Iron tier
    }
    
    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
    	return true;
    }
    
    @Override
    public Item getItemDropped(int par1, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @Override
    public boolean hasSeperateInventoryBlockIcon() {
        return true;
    }

}
