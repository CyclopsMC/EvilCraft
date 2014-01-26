package evilcraft.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.fluids.Blood;

public class BloodContainer extends ConfigurableDamageIndicatedItemFluidContainer {
    
    private static BloodContainer _instance = null;
    
    private Icon[] icons = new Icon[BloodContainerConfig.getContainerLevels()];
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new BloodContainer(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static BloodContainer getInstance() {
        return _instance;
    }

    private BloodContainer(ExtendedConfig eConfig) {
        super(eConfig, BloodExtractorConfig.containerSize, Blood.getInstance());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        for(int i = 0; i < icons.length; i++) {
            icons[i] = iconRegister.registerIcon(getIconString() + "_" + i);
        }
    }
    
    @Override
    public Icon getIconFromDamage(int damage) {
        return icons[Math.min(damage, icons.length - 1)];
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tab, List itemList) {
        for(int i = 0; i < icons.length; i++) {
            component.getSubItems(id, tab, itemList, fluid, i);
        }
    }
    
    @Override
    public int getCapacity(ItemStack container) {
        return capacity << (container.getItemDamage());
    }
    
    public String getItemDisplayName(ItemStack itemStack) {
        return BloodContainerConfig.containerLevelNames[Math.min(itemStack.getItemDamage(), icons.length - 1)];
    }

}
