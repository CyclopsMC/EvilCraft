package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;

import javax.annotation.Nullable;
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
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!ItemStackHelpers.isValidCreativeTab(this, creativeTabs)) return;
        for(int i = 0; i < COLORS.size(); i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        return super.getTranslationKey(itemStack) + "." + itemStack.getItemDamage();
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
        public int colorMultiplier(ItemStack itemStack, int renderPass) {
            return COLORS.get(itemStack.getItemDamage());
        }
    }

}
