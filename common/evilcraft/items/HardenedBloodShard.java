package evilcraft.items;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class HardenedBloodShard extends ConfigurableItem {
    
    private static HardenedBloodShard _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new HardenedBloodShard(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static HardenedBloodShard getInstance() {
        return _instance;
    }

    private HardenedBloodShard(ExtendedConfig eConfig) {
        super(eConfig);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        list.add(IInformationProvider.INFO_PREFIX + "Use Flint and Steel on Hardened Blood.");
    }

}
