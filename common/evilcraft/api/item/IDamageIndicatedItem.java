package evilcraft.api.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public interface IDamageIndicatedItem {
    public void getSubItems(int id, CreativeTabs tab, List itemList);
}
