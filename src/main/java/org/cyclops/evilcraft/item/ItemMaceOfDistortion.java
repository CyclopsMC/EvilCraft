package org.cyclops.evilcraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.Advancements;
import org.cyclops.evilcraft.ExtendedDamageSources;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A powerful magical mace.
 * The power of it can be changed by Shift + Right clicking.
 * It can be used as primary weapon by just right clicking on entities, it will however
 * use up some blood for that and become unusable when the tank is empty.
 * It can also be used as secondary weapon to do a distortion effect in a certain area
 * with the area increasing depending on how long the item is being charged, this
 * area is smaller with a larger power level, but more powerful.
 * @author rubensworks
 *
 */
public class ItemMaceOfDistortion extends ItemMace {

    /**
     * The amount of ticks that should go between each update of the area of effect particles.
     */
    public static final int AOE_TICK_UPDATE = 20;

    private static final int MAXIMUM_CHARGE = 100;
    private static final float MELEE_DAMAGE = 7.0F;
    private static final float RADIAL_DAMAGE = 3.0F;
    private static final int CONTAINER_SIZE = FluidHelpers.BUCKET_VOLUME * 4;
    private static final int HIT_USAGE = 5;
    private static final int POWER_LEVELS = 5;

    public ItemMaceOfDistortion(Item.Properties properties) {
        super(properties, CONTAINER_SIZE, HIT_USAGE, MAXIMUM_CHARGE, POWER_LEVELS, MELEE_DAMAGE);
    }

    @SuppressWarnings("unchecked")
    protected void distortEntities(Level world, LivingEntity initiator, int itemUsedCount, int power) {
        // Center of the knockback
        double x = initiator.getX();
        double y = initiator.getY();
        double z = initiator.getZ();

        // Get the entities in the given area
        double area = getArea(itemUsedCount);
        AABB box = new AABB(x, y, z, x, y, z).inflate(area);
        List<Entity> entities = world.getEntities(initiator, box);

        // Do knockback and damage to the list of entities
        boolean onePlayer = false;
        for(Entity entity : entities) {
            if(entity instanceof Player) {
                onePlayer = true;
            }
            distortEntity(world, initiator, entity, x, y, z, itemUsedCount, power);
        }

        if (initiator instanceof ServerPlayer) {
            Advancements.DISTORT.test((ServerPlayer) initiator, entities);
        }
    }

    /**
     * Distort an entity.
     * @param world The world.
     * @param initiator The player distoring the entity, can be null.
     * @param entity The distorted entity.
     * @param x Center X coordinate.
     * @param y Center Y coordinate.
     * @param z Center Z coordinate.
     * @param itemUsedCount The distortion usage power.
     * @param power The current power.
     */
    public void distortEntity(Level world, @Nullable LivingEntity initiator, Entity entity, double x, double y, double z, int itemUsedCount, int power) {
        double inverseStrength = initiator != null ? entity.distanceTo(initiator) / (itemUsedCount + 1) : 0.1;
        double knock = power + itemUsedCount / 200 + 1.0D;

        double dx = entity.getX() - x;
        double dy = entity.getY() + (double)entity.getEyeHeight() - y;
        double dz = entity.getZ() - z;
        double d = Mth.sqrt((float) (dx * dx + dy * dy + dz * dz));

        // No knockback is possible when the absolute distance is zero.
        if (d != 0.0D) {
            dx /= d;
            dy /= d;
            dz /= d;
            double strength = (1.0D - inverseStrength) * knock;
            if(entity instanceof LivingEntity) {
                // Attack the entity with the current power level.
                DamageSource damageSource;
                if(initiator == null) {
                    damageSource = ExtendedDamageSources.distorted(world);
                } else {
                    if(initiator instanceof Player) {
                        damageSource = initiator.damageSources().playerAttack((Player) initiator);
                    } else {
                        damageSource = initiator.damageSources().mobAttack(initiator);
                    }
                }
                entity.hurt(damageSource, RADIAL_DAMAGE * power);

                if(world.isClientSide()) {
                    showEntityDistored(world, initiator, entity, power);
                }
            }
            if(entity instanceof EntityVengeanceSpirit) {
                ((EntityVengeanceSpirit) entity).setSwarm(true);
            }
            strength /= 2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(strength, strength, strength));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void showEntityDistored(Level world, LivingEntity initiator, Entity entity, int power) {
        // Play a nice sound with the volume depending on the power.
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, (float) (power + 1) / (float) POWER_LEVELS, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        if(initiator != null) {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, (float) (power + 1) / (float) POWER_LEVELS, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
        }
        // Fake explosion effect.
        world.addParticle(ParticleTypes.EXPLOSION, entity.getX(), entity.getY() + world.random.nextFloat(), entity.getZ(), 1.0D, 0.0D, 0.0D);
    }

    @OnlyIn(Dist.CLIENT)
    protected void animateOutOfEnergy(Level world, Player player) {
        double xCoord = player.getX();
        double yCoord = player.getY();
        double zCoord = player.getZ();

        float particleMotionX = world.random.nextFloat() * 0.2F - 0.1F;
        float particleMotionY = 0.2F;
        float particleMotionZ = world.random.nextFloat() * 0.2F - 0.1F;
        world.addParticle(ParticleTypes.SMOKE, xCoord, yCoord, zCoord, particleMotionX, particleMotionY, particleMotionZ);

        world.playSound(player, xCoord, yCoord, zCoord, SoundEvents.NOTE_BLOCK_BASEDRUM.value(), SoundSource.RECORDS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public int getEnchantmentValue(ItemStack itemStack) {
        return 15;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (slot == EquipmentSlot.MAINHAND) {
            return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", MELEE_DAMAGE, AttributeModifier.Operation.ADDITION));
        }
        return super.getAttributeModifiers(slot, itemStack);
    }

    @Override
    protected void use(Level world, LivingEntity entity, int itemUsedCount, int power) {
        distortEntities(world, entity, itemUsedCount, power);
    }
}
