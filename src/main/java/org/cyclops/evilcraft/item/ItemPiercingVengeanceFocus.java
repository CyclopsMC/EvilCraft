package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.entity.effect.EntityAttackVengeanceBeam;

/**
 * Focus that is able attack vengeance spirits.
 * @author rubensworks
 *
 */
public class ItemPiercingVengeanceFocus extends ItemAbstractFocus {

    public ItemPiercingVengeanceFocus(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    protected ThrowableProjectile newBeamEntity(LivingEntity player) {
        return new EntityAttackVengeanceBeam(player.level(), player);
    }
}
