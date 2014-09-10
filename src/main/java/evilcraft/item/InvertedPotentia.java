package evilcraft.item;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.core.IInformationProvider;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * An inverted {@link PotentiaSphere}.
 * @author rubensworks
 *
 */
public class InvertedPotentia extends ConfigurableItem {
    
    private static InvertedPotentia _instance = null;
    
    /**
     * Meta data for the empowered state.
     */
    public static final int EMPOWERED_META = 1;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new InvertedPotentia(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static InvertedPotentia getInstance() {
        return _instance;
    }

    private InvertedPotentia(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return isEmpowered(itemStack);
    }
    
    /**
     * Set the given ItemStack as an empowered {@link InvertedPotentia}.
     * @param itemStack The ItemStack to check.
     */
    public static void empower(ItemStack itemStack) {
        if(itemStack.getItem() == InvertedPotentia.getInstance()) {
            itemStack.setItemDamage(EMPOWERED_META);
        }
    }
    
    /**
     * If the given ItemStack is an empowered {@link InvertedPotentia}.
     * @param itemStack The ItemStack to check.
     * @return If it is an empowered {@link InvertedPotentia}.
     */
    public static boolean isEmpowered(ItemStack itemStack) {
        if(itemStack.getItem() == InvertedPotentia.getInstance()) {
            return itemStack.getItemDamage() == EMPOWERED_META;
        }
        return false;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(isEmpowered(itemStack))
            list.add(EnumChatFormatting.RED + "Empowered");
        list.add(IInformationProvider.INFO_PREFIX + "Strike with lightning to empower.");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < 2; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

}
