package evilcraft.blocks;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableBlockSapling;

public class UndeadSapling extends ConfigurableBlockSapling implements IInformationProvider {
    
    private static UndeadSapling _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new UndeadSapling(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static UndeadSapling getInstance() {
        return _instance;
    }

    private UndeadSapling(ExtendedConfig eConfig) {
        super(eConfig, Material.plants);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return IInformationProvider.INFO_PREFIX + "Infuse dead bush with blood.";
    }

    @Override
    public void provideInformation(ItemStack itemStack,
            EntityPlayer entityPlayer, List list, boolean par4) {}

}
