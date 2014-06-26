package evilcraft.items;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.L10NHelpers;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.blocks.HardenedBlood;

/**
 * Shard created from {@link HardenedBlood}.
 * @author rubensworks
 *
 */
public class HardenedBloodShard extends ConfigurableItem {
    
    private static HardenedBloodShard _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new HardenedBloodShard(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static HardenedBloodShard getInstance() {
        return _instance;
    }

    private HardenedBloodShard(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        list.add(L10NHelpers.getLocalizedInfo(this));
    }

}
