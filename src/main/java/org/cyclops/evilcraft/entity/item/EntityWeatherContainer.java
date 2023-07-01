package org.cyclops.evilcraft.entity.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;
import org.cyclops.evilcraft.item.ItemWeatherContainer;

import javax.annotation.Nonnull;

/**
 * Entity for the {@link ItemWeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityWeatherContainer extends EntityThrowable {

    private static final EntityDataAccessor<ItemStack> ITEMSTACK_INDEX = SynchedEntityData.<ItemStack>defineId(EntityWeatherContainer.class, EntityDataSerializers.ITEM_STACK);

    public EntityWeatherContainer(EntityType<? extends EntityWeatherContainer> type, Level world) {
        super(type, world);
    }

    public EntityWeatherContainer(Level world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_WEATHER_CONTAINER, world, entity);
    }

    public EntityWeatherContainer(Level world, LivingEntity entity, ItemStack stack) {
        this(world, entity);
        setItem(stack);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void playImpactSounds(Level world) {
        if (!world.isClientSide()) {
            // Play evil sounds at the players in that world
            for(Object o : world.players()) {
                Player entityPlayer = (Player) o;
                world.playSound(entityPlayer, entityPlayer.getX(), entityPlayer.getY(), entityPlayer.getZ(), SoundEvents.PORTAL_TRAVEL, SoundSource.WEATHER, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
                world.playSound(entityPlayer, entityPlayer.getX(), entityPlayer.getY(), entityPlayer.getZ(), SoundEvents.GHAST_AMBIENT, SoundSource.WEATHER, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
                world.playSound(entityPlayer, entityPlayer.getX(), entityPlayer.getY(), entityPlayer.getZ(), SoundEvents.WITHER_DEATH, SoundSource.WEATHER, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
            }
        }
    }

    @Override
    protected void onHit(HitResult movingobjectposition) {
        if (movingobjectposition.getType() == HitResult.Type.BLOCK) {
            ItemStack stack = getItem();
            ItemWeatherContainer.WeatherContainerType containerType = ItemWeatherContainer.getWeatherType(stack);
            if (level() instanceof ServerLevel) {
                containerType.onUse((ServerLevel) level(), stack);
            }

            playImpactSounds(level());

            // Play sound and show particles of splash potion of harming
            level().playLocalSound(getX(), getY(), getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.AMBIENT, 0.5F, 0.4F, false);
            for (int i = 0; i < 3; i++) {
                level().addParticle(ParticleTypes.EFFECT, getX(), getY(), getZ(), 0, 0, 0);
            }

            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected float getGravity() {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }

    private void setItem(ItemStack stack) {
        entityData.set(ITEMSTACK_INDEX, stack);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(ITEMSTACK_INDEX, new ItemStack(RegistryEntries.ITEM_WEATHER_CONTAINER));
    }

    @Override
    public ItemStack getItem() {
        return entityData.get(ITEMSTACK_INDEX);
    }
}
