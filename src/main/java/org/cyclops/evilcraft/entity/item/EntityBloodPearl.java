package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemBloodPearlOfTeleportation;
import org.cyclops.evilcraft.item.ItemBloodPearlOfTeleportationConfig;

import javax.annotation.Nonnull;

/**
 * Entity for the {@link ItemBloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityBloodPearl extends ThrowableEntity implements IRendersAsItem {

    public EntityBloodPearl(EntityType<? extends EntityBloodPearl> type, World world) {
        super(type, world);
    }

    public EntityBloodPearl(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_BLOOD_PEARL, entity, world);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onImpact(RayTraceResult position) {
        if (position.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) position).getEntity()
                    .attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0.0F);
        }

        if (!this.world.isRemote()) {
            if (this.func_234616_v_() != null && this.func_234616_v_() instanceof ServerPlayerEntity) {
                ServerPlayerEntity entityplayermp = (ServerPlayerEntity)this.func_234616_v_();

                if (entityplayermp.connection.netManager.isChannelOpen() && entityplayermp.world == this.world) {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        if (this.func_234616_v_().isPassenger()) {
                            this.func_234616_v_().stopRiding();
                        }
    
                        this.func_234616_v_().setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        this.func_234616_v_().fallDistance = 0.0F;
                        this.func_234616_v_().attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
                        ((LivingEntity) this.func_234616_v_()).addPotionEffect(new EffectInstance(Effects.SLOWNESS,
                        		ItemBloodPearlOfTeleportationConfig.slownessDuration * 20, 2));
                    }
                }
            }

            this.remove();
        } else {
            for (int i = 0; i < 32; ++i) {
                Minecraft.getInstance().worldRenderer.addParticle(
                        ParticleTypes.PORTAL, false,
                        this.getPosX(), this.getPosY() + this.rand.nextDouble() * 2.0D, this.getPosZ(),
                        this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
            }
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_BLOOD_PEARL_OF_TELEPORTATION);
    }
}
