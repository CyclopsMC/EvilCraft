package evilcraft.blocks;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.configurable.ConfigurableBlockConnectedTexture;

/**
 * Glass that holds back some light.
 * @author rubensworks
 *
 */
public class ObscuredGlass extends ConfigurableBlockConnectedTexture {
    
    private static ObscuredGlass _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new ObscuredGlass(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ObscuredGlass getInstance() {
        return _instance;
    }

    private ObscuredGlass(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.glass);
        this.setHardness(0.5F);
        this.setStepSound(soundTypeGlass);
        this.setLightOpacity(10);
    }
    
    @Override
    public boolean hasSeperateInventoryBlockIcon() {
        return true;
    }
    
    @Override
    public Item getItemDropped(int par1, Random random, int zero) {
        return Item.getItemFromBlock(this);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side) {
        Block block = world.getBlock(x, y, z);
        return block == this ? false : super.shouldSideBeRendered(world, x, y, z, side);
    }
    
    @Override
    public int getRenderBlockPass() {
        return 1;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

}
