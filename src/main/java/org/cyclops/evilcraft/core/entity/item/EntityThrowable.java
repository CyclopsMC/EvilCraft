package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An extension of {@link ThrowableEntity} that now
 * also has an inner {@link ItemStack} that must be implemented.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public abstract class EntityThrowable extends ThrowableEntity implements IRendersAsItem {

    public EntityThrowable(EntityType<? extends EntityThrowable> type, World world) {
        super(type, world);
    }

    public EntityThrowable(EntityType<? extends EntityThrowable> type, World world, LivingEntity entity) {
        super(type, entity, world);
    }

}
