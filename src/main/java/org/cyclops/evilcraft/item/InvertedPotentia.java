package org.cyclops.evilcraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;

import java.util.List;

/**
 * An inverted Potentia Sphere.
 * @author rubensworks
 *
 */
public class InvertedPotentia extends ConfigurableItem implements IItemEmpowerable {
    
    private static InvertedPotentia _instance = null;
    
    /**
     * Meta data for the empowered state.
     */
    public static final int EMPOWERED_META = 1;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static InvertedPotentia getInstance() {
        return _instance;
    }

    public InvertedPotentia(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return isEmpowered(itemStack);
    }
    
    @Override
	public ItemStack empower(ItemStack itemStack) {
        if(itemStack.getItem() == InvertedPotentia.getInstance()) {
            itemStack.setItemDamage(EMPOWERED_META);
        }
        return itemStack;
    }
    
    @Override
	public boolean isEmpowered(ItemStack itemStack) {
        return itemStack.getItem() == this && itemStack.getItemDamage() == EMPOWERED_META;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(isEmpowered(itemStack))
            list.add(TextFormatting.RED + "Empowered");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for(int i = 0; i < 2; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemEmpowerable(world, (EntityItem) location);
    }

}
