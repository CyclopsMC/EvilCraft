package org.cyclops.evilcraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;

/**
 * An inverted Potentia Sphere.
 * @author rubensworks
 *
 */
public class ItemInvertedPotentia extends Item implements IItemEmpowerable {

    private final boolean empowered;

    public ItemInvertedPotentia(Properties properties, boolean empowered) {
        super(properties);
        this.empowered = empowered;
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return isEmpowered(itemStack);
    }
    
    @Override
	public ItemStack empower(ItemStack itemStack) {
        return new ItemStack(RegistryEntries.ITEM_INVERTED_POTENTIA_EMPOWERED);
    }
    
    @Override
	public boolean isEmpowered(ItemStack itemStack) {
        return this.empowered;
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemEmpowerable(world, (ItemEntity) location);
    }

}
