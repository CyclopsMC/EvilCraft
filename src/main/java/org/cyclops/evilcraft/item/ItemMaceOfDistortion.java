package org.cyclops.evilcraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.Advancements;
import org.cyclops.evilcraft.ExtendedDamageSource;
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
    protected void distortEntities(World world, LivingEntity initiator, int itemUsedCount, int power) {
        // Center of the knockback
        double x = initiator.getPosX();
        double y = initiator.getPosY();
        double z = initiator.getPosZ();
        
        // Get the entities in the given area
        double area = getArea(itemUsedCount);
        AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).grow(area);
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(initiator, box);
        
        // Do knockback and damage to the list of entities
        boolean onePlayer = false;
        for(Entity entity : entities) {
        	if(entity instanceof PlayerEntity) {
        		onePlayer = true;
        	}
            distortEntity(world, initiator, entity, x, y, z, itemUsedCount, power);
        }

        if (initiator instanceof ServerPlayerEntity) {
            Advancements.DISTORT.test((ServerPlayerEntity) initiator, entities);
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
    public void distortEntity(World world, @Nullable LivingEntity initiator, Entity entity, double x, double y, double z, int itemUsedCount, int power) {
        double inverseStrength = initiator != null ? entity.getDistance(initiator) / (itemUsedCount + 1) : 0.1;
        double knock = power + itemUsedCount / 200 + 1.0D;

        double dx = entity.getPosX() - x;
        double dy = entity.getPosY() + (double)entity.getEyeHeight() - y;
        double dz = entity.getPosZ() - z;
        double d = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);

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
                    damageSource = ExtendedDamageSource.distorted;
                } else {
                    if(initiator instanceof PlayerEntity) {
                        damageSource = DamageSource.causePlayerDamage((PlayerEntity) initiator);
                    } else {
                        damageSource = DamageSource.causeMobDamage(initiator);
                    }
                }
                entity.attackEntityFrom(damageSource, RADIAL_DAMAGE * power);
                
                if(world.isRemote()) {
                    showEntityDistored(world, initiator, entity, power);
                }
            }
            if(entity instanceof EntityVengeanceSpirit) {
            	((EntityVengeanceSpirit) entity).setSwarm(true);
            }
            strength /= 2;
            entity.setMotion(entity.getMotion().mul(strength, strength, strength));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void showEntityDistored(World world, LivingEntity initiator, Entity entity, int power) {
        // Play a nice sound with the volume depending on the power.
        world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, (float) (power + 1) / (float) POWER_LEVELS, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if(initiator != null) {
            world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, (float) (power + 1) / (float) POWER_LEVELS, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        }
        // Fake explosion effect.
        world.addParticle(ParticleTypes.EXPLOSION, entity.getPosX(), entity.getPosY() + random.nextFloat(), entity.getPosZ(), 1.0D, 0.0D, 0.0D);
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void animateOutOfEnergy(World world, PlayerEntity player) {
        double xCoord = player.getPosX();
        double yCoord = player.getPosY();
        double zCoord = player.getPosZ();

        float particleMotionX = world.rand.nextFloat() * 0.2F - 0.1F;
        float particleMotionY = 0.2F;
        float particleMotionZ = world.rand.nextFloat() * 0.2F - 0.1F;
        world.addParticle(ParticleTypes.SMOKE, xCoord, yCoord, zCoord, particleMotionX, particleMotionY, particleMotionZ);
        
        world.playSound(player, xCoord, yCoord, zCoord, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.RECORDS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    }
    
    @Override
    public int getItemEnchantability() {
        return 15;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Multimap getAttributeModifiers(EquipmentSlotType slot, ItemStack itemStack) {
        if (slot == EquipmentSlotType.MAINHAND) {
            return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", MELEE_DAMAGE, AttributeModifier.Operation.ADDITION));
        }
        return super.getAttributeModifiers(slot, itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void use(World world, LivingEntity entity, int itemUsedCount, int power) {
        distortEntities(world, entity, itemUsedCount, power);
    }
}
