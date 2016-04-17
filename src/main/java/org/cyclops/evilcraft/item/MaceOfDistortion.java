package org.cyclops.evilcraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;

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
public class MaceOfDistortion extends Mace {
    
    private static MaceOfDistortion _instance = null;
    
    /**
     * The amount of ticks that should go between each update of the area of effect particles.
     */
    public static final int AOE_TICK_UPDATE = 20;
    
    private static final int MAXIMUM_CHARGE = 100;
    private static final float MELEE_DAMAGE = 7.0F;
    private static final float RADIAL_DAMAGE = 3.0F;
    private static final int CONTAINER_SIZE = FluidContainerRegistry.BUCKET_VOLUME * 4;
    private static final int HIT_USAGE = 5;
    private static final int POWER_LEVELS = 5;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static MaceOfDistortion getInstance() {
        return _instance;
    }

    public MaceOfDistortion(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CONTAINER_SIZE, HIT_USAGE, MAXIMUM_CHARGE, POWER_LEVELS, MELEE_DAMAGE);
    }
    
    @SuppressWarnings("unchecked")
    protected void distortEntities(World world, EntityLivingBase initiator, int itemUsedCount, int power) {
        // Center of the knockback
        double x = initiator.posX;
        double y = initiator.posY;
        double z = initiator.posZ;
        
        // Get the entities in the given area
        double area = getArea(itemUsedCount);
        AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).expand(area, area, area);
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(initiator, box);
        
        // Do knockback and damage to the list of entities
        boolean onePlayer = false;
        for(Entity entity : entities) {
        	if(entity instanceof EntityPlayer) {
        		onePlayer = true;
        	}
            distortEntity(world, initiator, entity, x, y, z, itemUsedCount, power);
        }
        
        if(entities.size() >= 1 && initiator instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) initiator;
            if(entities.size() >= 10) {
                player.addStat(Achievements.DISTORTER, 1);
            }
        	if(onePlayer) {
                player.addStat(Achievements.PLAYER_DISTORTER, 1);
        	}
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
    public void distortEntity(World world, EntityLivingBase initiator, Entity entity, double x, double y, double z, int itemUsedCount, int power) {
        double inverseStrength = entity.getDistance(x, y, z) / (itemUsedCount + 1);
        double knock = power + itemUsedCount / 200 + 1.0D;

        double dx = entity.posX - x;
        double dy = entity.posY + (double)entity.getEyeHeight() - y;
        double dz = entity.posZ - z;
        double d = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);

        // No knockback is possible when the absolute distance is zero.
        if (d != 0.0D) {
            dx /= d;
            dy /= d;
            dz /= d;
            double strength = (1.0D - inverseStrength) * knock;
            if(entity instanceof EntityLivingBase) {
                // Attack the entity with the current power level.
                DamageSource damageSource;
                if(initiator == null) {
                    damageSource = ExtendedDamageSource.distorted;
                } else {
                    if(initiator instanceof EntityPlayer) {
                        damageSource = DamageSource.causePlayerDamage((EntityPlayer) initiator);
                    } else {
                        damageSource = DamageSource.causeMobDamage(initiator);
                    }
                }
                entity.attackEntityFrom(damageSource, RADIAL_DAMAGE * power);
                
                if(world.isRemote) {
                    showEntityDistored(world, initiator, entity, power);
                }
            }
            if(entity instanceof VengeanceSpirit) {
            	((VengeanceSpirit) entity).setIsSwarm(true);
            }
            if(initiator != null && entity instanceof EntityPlayer && initiator instanceof EntityPlayer) {
                ((EntityPlayer) initiator).addStat(Achievements.PLAYER_DISTORTER, 1);
            }
            strength /= 2;
            entity.motionX += dx * strength;
            entity.motionY += dy * strength;
            entity.motionZ += dz * strength;
        }
    }
    
    @SideOnly(Side.CLIENT)
    protected static void showEntityDistored(World world, EntityLivingBase initiator, Entity entity, int power) {
        // Play a nice sound with the volume depending on the power.
        world.playSound(null, entity.posX, entity.posY, entity.posZ, new SoundEvent(new ResourceLocation("entity.generic.explode")), SoundCategory.BLOCKS, (float) (power + 1) / (float) POWER_LEVELS, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if(initiator != null) {
            world.playSound(null, entity.posX, entity.posY, entity.posZ, new SoundEvent(new ResourceLocation("entity.generic.explode")), SoundCategory.BLOCKS, (float) (power + 1) / (float) POWER_LEVELS, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }
        // Fake explosion effect.
        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY + itemRand.nextFloat(), entity.posZ, 1.0D, 0.0D, 0.0D);
    }
    
    @SideOnly(Side.CLIENT)
    protected void animateOutOfEnergy(World world, EntityPlayer player) {
        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;

        float particleMotionX = world.rand.nextFloat() * 0.2F - 0.1F;
        float particleMotionY = 0.2F;
        float particleMotionZ = world.rand.nextFloat() * 0.2F - 0.1F;
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                new EntitySmokeFX.Factory().getEntityFX(0, world, xCoord, yCoord, zCoord,
                        particleMotionX, particleMotionY, particleMotionZ)
                );
        
        world.playSound(player, xCoord, yCoord, zCoord, new SoundEvent(new ResourceLocation("note.bd")), SoundCategory.RECORDS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    }
    
    @Override
    public int getItemEnchantability() {
        return 15;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Multimap getAttributeModifiers(EntityEquipmentSlot slot, ItemStack itemStack) {
        Multimap multimap = super.getAttributeModifiers(slot, itemStack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) MELEE_DAMAGE, 0));
        }
        return multimap;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void use(World world, EntityLivingBase entity, int itemUsedCount, int power) {
        distortEntities(world, entity, itemUsedCount, power);
    }
}
