package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemLightningGrenade;

import javax.annotation.Nonnull;

/**
 * Entity for the {@link ItemLightningGrenade}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityLightningGrenade extends ThrowableEntity implements IRendersAsItem {

    public EntityLightningGrenade(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_LIGHTNING_GRENADE, entity, world);
    }

    public EntityLightningGrenade(EntityType<? extends EntityLightningGrenade> type, World world) {
        super(type, world);
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(RayTraceResult par1MovingObjectPosition) {
        if (par1MovingObjectPosition.getType() == RayTraceResult.Type.ENTITY) {
            ((EntityRayTraceResult) par1MovingObjectPosition).getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
        }

        if (!this.level.isClientSide()) {
            if (this.getOwner() != null && this.getOwner() instanceof ServerPlayerEntity) {
                EntityHelpers.onEntityCollided(this.level, new BlockPos(par1MovingObjectPosition.getLocation()), this);
                LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(level);
                bolt.moveTo(this.getX(), this.getY(), this.getZ());
            }

            this.remove();
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
    protected void defineSynchedData() {

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryEntries.ITEM_LIGHTNING_GRENADE);
    }
}
