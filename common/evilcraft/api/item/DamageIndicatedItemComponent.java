package evilcraft.api.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * A component that has to be added for classes that want to implement the DamageIndicator behaviour.
 * This could be for example an Item or an ItemFluidContainer.
 * @author rubensworks
 *
 */
public class DamageIndicatedItemComponent implements IDamageIndicatedItem{
    
    public Item item;
    public int capacity;

    public DamageIndicatedItemComponent(Item item, int capacity)
    {
        this.item = item;
        this.capacity = capacity;
        item.setMaxStackSize(1);
        item.setMaxDamage(102);
        item.setNoRepair();
    }
    
    /**
     * Updates the damage of the given stack with the given amount depending on the predefined item.
     */
    public void updateDamage(ItemStack itemStack, int amount)
    {
        item.setDamage(itemStack, 101 - ((amount * 100) / (this.capacity)));
    }
    
    /**
     * Make sure the full and empty container is available is the CreativeTab
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tab, List itemList)
    {
        ItemStack itemStackFull = new ItemStack(item, 1);
        this.updateDamage(itemStackFull, capacity);
        itemList.add(itemStackFull);
        ItemStack itemStackEmpty = new ItemStack(item, 1);
        this.updateDamage(itemStackEmpty, 0);
        itemList.add(itemStackEmpty);
    }
    
}
