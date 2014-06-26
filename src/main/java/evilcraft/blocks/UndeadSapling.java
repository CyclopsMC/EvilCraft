package evilcraft.blocks;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import evilcraft.api.IInformationProvider;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockSapling;

/**
 * Sapling for the Undead Tree.
 * @author rubensworks
 *
 */
public class UndeadSapling extends ConfigurableBlockSapling implements IInformationProvider {
    
    private static UndeadSapling _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<BlockConfig> eConfig) {
        if(_instance == null)
            _instance = new UndeadSapling(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static UndeadSapling getInstance() {
        return _instance;
    }

    private UndeadSapling(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.plants);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
    	return L10NHelpers.getLocalizedInfo(this);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}

}
