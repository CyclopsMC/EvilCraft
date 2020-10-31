package org.cyclops.evilcraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;

/**
 * A dark stick.
 * @author rubensworks
 *
 */
public class ItemDarkStick extends Item {

    public ItemDarkStick(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
        return new EntityItemDarkStick(world, (ItemEntity) location);
    }

}
