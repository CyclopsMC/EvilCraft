package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Blood reactant.
 * 
 * @author rubensworks
 *
 */
public class PromiseAcceptor extends ConfigurableItem {

    private static PromiseAcceptor _instance = null;
    public static final Map<Integer, Integer> COLORS = Maps.newHashMap();
    static {
        COLORS.put(0, Helpers.RGBToInt(255, 255, 255));
        COLORS.put(1, Helpers.RGBToInt(230, 230, 160));
        COLORS.put(2, Helpers.RGBToInt(150, 250, 200));
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PromiseAcceptor getInstance() {
        return _instance;
    }

    public PromiseAcceptor(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < COLORS.size(); i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName(itemStack) + "." + itemStack.getItemDamage();
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return new ItemColor();
    }

    @SideOnly(Side.CLIENT)
    public static class ItemColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack itemStack, int renderPass) {
            return COLORS.get(itemStack.getItemDamage());
        }
    }

}
