package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public Entity createEntity(Level world, Entity location, ItemStack itemStack) {
        return new EntityItemDarkStick(world, (ItemEntity) location);
    }

}
