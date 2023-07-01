package org.cyclops.evilcraft.entity.effect;

import com.google.common.collect.Lists;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Advancements;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombie;
import org.cyclops.evilcraft.item.ItemNecromancerStaff;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

/**
 * Entity for the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class EntityNecromancersHead extends ThrowableProjectile implements ItemSupplier {

    private static final int DURATION = 200;
    private static final ItemStack RENDER_ITEM = new ItemStack(Items.SKELETON_SKULL);

    protected boolean observing = false;
    protected LivingEntity target = null;
    protected List<EntityControlledZombie> observables = Lists.newLinkedList();
    protected Class<? extends Mob> mobType = EntityControlledZombie.class;

    public EntityNecromancersHead(EntityType<? extends EntityNecromancersHead> type, Level world) {
        super(type, world);
    }

    public EntityNecromancersHead(Level world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_NECROMANCER_HEAD, entity, world);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Set the type of mob to be spawned. Should only be called server-side.
     * @param mobType The mob type.
     */
    public void setMobType(Class<? extends Mob> mobType) {
        this.mobType = mobType;
    }

    protected void spawnSwarm(LivingEntity necromancer, LivingEntity target) {
        Level world = target.level();
        int amount = world.random.nextInt(2) + 3;
        for(int i = 0; i < amount; i++) {
            EntityControlledZombie mob = new EntityControlledZombie(world);
            if(mob.canAttackType(target.getType())) {
                mob.copyPosition(necromancer);
                mob.move(MoverType.SELF, new Vec3(world.random.nextInt(20) - 10, 0, world.random.nextInt(20) - 10));
                if(EntityHelpers.spawnEntity((ServerLevel) world, mob, MobSpawnType.MOB_SUMMONED)) {
                    observables.add(mob);
                }
                mob.setTarget(target);
                mob.setTtl(DURATION);
            }
        }
        this.target = target;
        setObserverMode();

        if (necromancer instanceof ServerPlayer) {
            Advancements.NECROMANCE.test((ServerPlayer) necromancer, target);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(observing && !level().isClientSide() && WorldHelpers.efficientTick(level(), 10)) {
            if(!observables.isEmpty()) {
                Iterator<EntityControlledZombie> it = observables.iterator();
                while(it.hasNext()) {
                    EntityControlledZombie mob = it.next();
                    if(!mob.isAlive() || !target.isAlive()) {
                        if(mob.isAlive()) {
                            mob.remove(RemovalReason.DISCARDED);
                        }
                        it.remove();
                    }
                }
            }
            if(observables.isEmpty()) {
                observing = false;
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if(!observing) {
            super.remove(removalReason);
        }
    }

    protected void setObserverMode() {
        observing = true;
        setDeltaMovement(0, 0, 0);
        setInvisible(true);
    }

    @Override
    protected void onHit(HitResult position) {
        if(position.getType() == HitResult.Type.ENTITY && !observing && !getCommandSenderWorld().isClientSide()) {
            ((EntityHitResult) position).getEntity().hurt(level().damageSources().thrown(this, this.getOwner()), 0.0F);
            if(getOwner() instanceof ServerPlayer
                    && getOwner() != ((EntityHitResult) position).getEntity()
                    && ((EntityHitResult) position).getEntity() instanceof LivingEntity) {
                spawnSwarm((LivingEntity) this.getOwner(), (LivingEntity) ((EntityHitResult) position).getEntity());
            } else {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return RENDER_ITEM;
    }
}
