package org.cyclops.evilcraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    protected ThrowableEntity newBeamEntity(LivingEntity player) {
        return new EntityAttackVengeanceBeam(player.level, player);
    }
}
