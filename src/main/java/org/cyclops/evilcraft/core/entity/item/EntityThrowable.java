package org.cyclops.evilcraft.core.entity.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An extension of {@link ThrowableProjectile} that now
 * also has an inner {@link ItemStack} that must be implemented.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public abstract class EntityThrowable extends ThrowableProjectile implements ItemSupplier {

    public EntityThrowable(EntityType<? extends EntityThrowable> type, Level world) {
        super(type, world);
    }

    public EntityThrowable(EntityType<? extends EntityThrowable> type, Level world, LivingEntity entity) {
        super(type, entity, world);
    }

}
