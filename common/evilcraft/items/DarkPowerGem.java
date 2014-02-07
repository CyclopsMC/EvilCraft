package evilcraft.items;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.IInformationProvider;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableItem;

public class DarkPowerGem extends ConfigurableItem {
    
    private static DarkPowerGem _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new DarkPowerGem(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static DarkPowerGem getInstance() {
        return _instance;
    }

    private DarkPowerGem(ExtendedConfig eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        list.add(IInformationProvider.INFO_PREFIX + "Throw a Dark Gem in a pool of at least five non-hardened Blood blocks.");
        list.add(IInformationProvider.INFO_PREFIX + "Or infuse Dark Gem with blood.");
    }

}
