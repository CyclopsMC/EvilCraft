package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemLightningGrenade;

/**
 * Entity for the {@link ItemLightningGrenade}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class EntityLightningGrenade extends ThrowableProjectile implements ItemSupplier {

    public EntityLightningGrenade(Level world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_LIGHTNING_GRENADE.get(), entity, world);
    }

    public EntityLightningGrenade(EntityType<? extends EntityLightningGrenade> type, Level world) {
        super(type, world);
    }

    @Override
    protected void onHit(HitResult par1MovingObjectPosition) {
        if (par1MovingObjectPosition.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) par1MovingObjectPosition).getEntity().hurt(level().damageSources().thrown(this, this.getOwner()), 0.0F);
        }

        if (!this.level().isClientSide()) {
            if (this.getOwner() != null && this.getOwner() instanceof ServerPlayer) {
                BlockPos pos = BlockPos.containing(par1MovingObjectPosition.getLocation());
                EntityHelpers.onEntityCollided(this.level(), pos, this.level().getBlockState(pos), this);
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level());
                bolt.moveTo(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(bolt);
            }

            this.remove(RemovalReason.DISCARDED);
        } else {
            for (int i = 0; i < 32; ++i) {
                Minecraft.getInstance().levelRenderer.addParticle(
                        ParticleTypes.CRIT, false,
                        this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(),
                        this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_LIGHTNING_GRENADE);
    }
}
