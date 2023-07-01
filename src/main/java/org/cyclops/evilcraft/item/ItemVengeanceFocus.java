package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeam;

/**
 * Focus that is able to direct rays of the sun to entangle vengeance spirits.
 * @author rubensworks
 *
 */
public class ItemVengeanceFocus extends ItemAbstractFocus {

    public ItemVengeanceFocus(Properties properties) {
        super(properties);
    }

    @Override
    protected ThrowableProjectile newBeamEntity(LivingEntity player) {
        return new EntityAntiVengeanceBeam(player.level(), player);
    }
}
