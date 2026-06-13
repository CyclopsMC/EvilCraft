package org.cyclops.evilcraft.item;

import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeam;
import org.cyclops.evilcraft.entity.effect.EntityAttackVengeanceBeam;

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
    protected ThrowableProjectile newBeamEntity(ServerLevel level, LivingEntity player, ItemStack itemStack) {
        return new EntityAntiVengeanceBeam(player.level(), player, itemStack);
    }

    @Override
    protected ThrowableProjectile newBeamEntity(Level level, Position position, ItemStack itemStack) {
        return new EntityAntiVengeanceBeam(level, position.x(), position.y(), position.z(), itemStack);
    }
}
