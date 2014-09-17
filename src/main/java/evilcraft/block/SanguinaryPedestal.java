package evilcraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.config.configurable.ConfigurableBlockContainer;
import evilcraft.core.config.extendedconfig.BlockConfig;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.tileentity.TileSanguinaryPedestal;

/**
 * Pedestal that can obtain blood from blood stained blocks and can optionally extract blood from mobs
 * when a blood extractor is inserted.
 * @author rubensworks
 *
 */
public class SanguinaryPedestal extends ConfigurableBlockContainer {
    
    private static SanguinaryPedestal _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new SanguinaryPedestal(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SanguinaryPedestal getInstance() {
        return _instance;
    }

    private SanguinaryPedestal(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileSanguinaryPedestal.class);
        setBlockBounds(0.02F, 0F, 0.02F, 0.98F, 0.98F, 0.98F);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        // This is ONLY used for the block breaking/broken particles
        // Since the anvil looks very similar, we use that icon.
        return Blocks.anvil.getIcon(0, 0);
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
    public boolean renderAsNormalBlock() {
        return false;
    }

}
