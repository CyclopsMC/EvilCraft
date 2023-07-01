package org.cyclops.evilcraft.entity.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockLightningBomb;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Entity for primed {@link BlockLightningBomb}.
 * @author rubensworks
 *
 */
public class EntityLightningBombPrimed extends PrimedTnt {

    private static final float EXPLOSION_STRENGTH = 1.0f;

    @Nullable
    private LivingEntity placer;

    public EntityLightningBombPrimed(EntityType<? extends EntityLightningBombPrimed> type, Level world) {
        super(type, world);
        setFuse();
    }

    public EntityLightningBombPrimed(Level world, double x, double y, double z, @Nullable LivingEntity placer) {
        this(RegistryEntries.ENTITY_LIGHTNING_BOMB_PRIMED, world);
        this.setPos(x, y, z);
        double d0 = world.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.placer = placer;
        setFuse();
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.placer;
    }

    protected void setFuse() {
        this.setFuse(EntityLightningBombPrimedConfig.fuse);
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        if (this.getFuse() - 1 <= 0) {
            this.remove(RemovalReason.KILLED);

            if (!this.level().isClientSide()) {
                this.explode(this.level(), this.getX(), this.getY(), this.getZ());
            }
        } else {
            setFuse(getFuse() - 1);
            this.doWaterSplashEffect();
            level().addParticle(ParticleTypes.SMOKE,
                    this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode(Level world, double x, double y, double z) {
        if (!world.isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_STRENGTH, Level.ExplosionInteraction.MOB);
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(world);
            bolt.moveTo(x, y, z);
        } else {
            Random rand = new Random();
            for (int i = 0; i < 32; ++i) {
                Minecraft.getInstance().levelRenderer.addParticle(
                        ParticleTypes.CRIT, false,
                        x, y + rand.nextDouble() * 2.0D, z,
                        rand.nextGaussian(), 0.0D, rand.nextGaussian());
            }
        }
    }

}
