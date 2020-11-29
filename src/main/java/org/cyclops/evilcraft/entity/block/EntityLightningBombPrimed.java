package org.cyclops.evilcraft.entity.block;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
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
public class EntityLightningBombPrimed extends TNTEntity {
    
    private static final float EXPLOSION_STRENGTH = 1.0f;

    @Nullable
    private LivingEntity placer;

    public EntityLightningBombPrimed(EntityType<? extends EntityLightningBombPrimed> type, World world) {
        super(type, world);
        setFuse();
    }

    public EntityLightningBombPrimed(World world, double x, double y, double z, @Nullable LivingEntity placer) {
        this(RegistryEntries.ENTITY_LIGHTNING_BOMB_PRIMED, world);
        this.setPosition(x, y, z);
        double d0 = world.rand.nextDouble() * (double)((float)Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.placer = placer;
        setFuse();
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    @Override
    public LivingEntity getTntPlacedBy() {
        return this.placer;
    }

    protected void setFuse() {
        this.setFuse(EntityLightningBombPrimedConfig.fuse);
    }
    
    @Override
    public void tick() {
        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98D));

        if (this.onGround) {
            this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
        }

        if (this.getFuse() - 1 <= 0) {
            this.remove();

            if (!this.world.isRemote()) {
                this.explode(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
            }
        } else {
            setFuse(getFuse() - 1);
            this.handleWaterMovement();
            world.addParticle(ParticleTypes.SMOKE,
                    this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode(World world, double x, double y, double z) {
        Random rand = new Random();
        for (int i = 0; i < 32; ++i) {
            Minecraft.getInstance().worldRenderer.addParticle(
                    ParticleTypes.CRIT, false,
                    x, y + rand.nextDouble() * 2.0D, z,
                    rand.nextGaussian(), 0.0D, rand.nextGaussian());
        }

        if (!world.isRemote()) {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), EXPLOSION_STRENGTH, Explosion.Mode.DESTROY);
            ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, x, y, z, false));
        }
    }

}
