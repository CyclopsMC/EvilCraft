package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.helper.FluidHelpers;

/**
 * A mace that produces explosions around the player, without damaging that player.
 * @author rubensworks
 *
 */
public class ItemMaceOfDestruction extends ItemMace {

    /**
     * The amount of ticks that should go between each update of the area of effect particles.
     */
    public static final int AOE_TICK_UPDATE = 20;

    private static final int MAXIMUM_CHARGE = 100;
    private static final float MELEE_DAMAGE = 10.0F;
    private static final int CONTAINER_SIZE = FluidHelpers.BUCKET_VOLUME * 4;
    private static final int HIT_USAGE = 6;
    private static final int POWER_LEVELS = 5;

    public ItemMaceOfDestruction(Item.Properties properties) {
        super(properties, CONTAINER_SIZE, HIT_USAGE, MAXIMUM_CHARGE, POWER_LEVELS, MELEE_DAMAGE);
    }

    @Override
    protected void use(Level world, LivingEntity entity, int itemUsedCount, int power) {
        if(!world.isClientSide()) {
            Vec3 v = entity.getLookAngle();
            world.explode(entity, entity.getX() + v.x * 2, entity.getY() + entity.getEyeHeight() + v.y * 2, entity.getZ() + v.z * 2, ((float) itemUsedCount) / 20 + power, Level.ExplosionInteraction.TNT);
        }
    }
}
