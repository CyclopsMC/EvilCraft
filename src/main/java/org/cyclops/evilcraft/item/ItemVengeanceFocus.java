package org.cyclops.evilcraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeam;

import net.minecraft.item.Item.Properties;

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
    protected ThrowableEntity newBeamEntity(LivingEntity player) {
        return new EntityAntiVengeanceBeam(player.level, player);
    }
}
