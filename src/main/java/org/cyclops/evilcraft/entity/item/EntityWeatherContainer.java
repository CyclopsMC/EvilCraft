package org.cyclops.evilcraft.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
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

    private static final DataParameter<ItemStack> ITEMSTACK_INDEX = EntityDataManager.<ItemStack>createKey(EntityWeatherContainer.class, DataSerializers.ITEMSTACK);

    public EntityWeatherContainer(EntityType<? extends EntityWeatherContainer> type, World world) {
        super(type, world);
    }

    public EntityWeatherContainer(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_WEATHER_CONTAINER, world, entity);
    }

    public EntityWeatherContainer(World world, LivingEntity entity, ItemStack stack) {
        this(world, entity);
        setItem(stack);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void playImpactSounds(World world) {
        if (!world.isRemote()) {
            // Play evil sounds at the players in that world
            for(Object o : world.getPlayers()) {
                PlayerEntity entityPlayer = (PlayerEntity) o;
                world.playSound(entityPlayer, entityPlayer.getPosX(), entityPlayer.getPosY(), entityPlayer.getPosZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.WEATHER, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
                world.playSound(entityPlayer, entityPlayer.getPosX(), entityPlayer.getPosY(), entityPlayer.getPosZ(), SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.WEATHER, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
                world.playSound(entityPlayer, entityPlayer.getPosX(), entityPlayer.getPosY(), entityPlayer.getPosZ(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.WEATHER, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult movingobjectposition) {
        if (movingobjectposition.getType() == RayTraceResult.Type.BLOCK) {
            ItemStack stack = getItem();
            ItemWeatherContainer.WeatherContainerType containerType = ItemWeatherContainer.getWeatherType(stack);
            if (world instanceof ServerWorld) {
                containerType.onUse((ServerWorld) world, stack);
            }

            playImpactSounds(world);

            // Play sound and show particles of splash potion of harming
            world.playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.AMBIENT, 0.5F, 0.4F, false);
            for (int i = 0; i < 3; i++) {
                world.addParticle(ParticleTypes.EFFECT, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
            }

            remove();
        }
    }
    
    @Override
    protected float getGravityVelocity() {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }
    
    private void setItem(ItemStack stack) {
        dataManager.set(ITEMSTACK_INDEX, stack);
    }
    
    @Override
    protected void registerData() {
        dataManager.register(ITEMSTACK_INDEX, new ItemStack(RegistryEntries.ITEM_WEATHER_CONTAINER));
    }

    @Override
    public ItemStack getItem() {
        return dataManager.get(ITEMSTACK_INDEX);
    }
}
