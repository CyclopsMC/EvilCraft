package org.cyclops.evilcraft.item;

import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    protected ThrowableProjectile newBeamEntity(ServerLevel level, LivingEntity player, ItemStack itemStack) {
        return new EntityAttackVengeanceBeam(player.level(), player, itemStack);
    }

    @Override
    protected ThrowableProjectile newBeamEntity(Level level, Position position, ItemStack itemStack) {
        return new EntityAttackVengeanceBeam(level, position.x(), position.y(), position.z(), itemStack);
    }
}
