package org.cyclops.evilcraft.entity.item;

import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;
import org.cyclops.evilcraft.item.WeatherContainer;
import org.cyclops.evilcraft.item.WeatherContainerConfig;

/**
 * Entity for the {@link WeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityWeatherContainer extends EntityThrowable implements IConfigurable {

    private static final DataParameter<Optional<ItemStack>> ITEMSTACK_INDEX = EntityDataManager.<Optional<ItemStack>>createKey(EntityWeatherContainer.class, DataSerializers.OPTIONAL_ITEM_STACK);
    
    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityWeatherContainer(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     * @param damage The damage value for the {@link WeatherContainer} to be rendered.
     */
    public EntityWeatherContainer(World world, EntityLivingBase entity, int damage) {
        this(world, entity, new ItemStack(Configs.isEnabled(WeatherContainerConfig.class) ? WeatherContainer.getInstance() : Items.coal, 1, damage));
    }

    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param entity The entity
     * @param stack The {@link ItemStack} inside this entity.
     */
    public EntityWeatherContainer(World world, EntityLivingBase entity, ItemStack stack) {
        super(world, entity);
        setItemStack(stack);
    }

    public static void playImpactSounds(World world) {
        if (!world.isRemote) {
            // Play evil sounds at the players in that world
            for(Object o : world.playerEntities) {
                EntityPlayer entityPlayer = (EntityPlayer) o;
                world.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.block_portal_travel, SoundCategory.WEATHER, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
                world.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.entity_ghast_ambient, SoundCategory.WEATHER, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
                world.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.entity_wither_death, SoundCategory.WEATHER, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult movingobjectposition) {
        ItemStack stack = getItemStack();
        WeatherContainer.WeatherContainerTypes containerType = WeatherContainer.getWeatherContainerType(stack);
        containerType.onUse(worldObj, stack);

        playImpactSounds(worldObj);
        
        // Play sound and show particles of splash potion of harming
        // TODO: make custom particles for this
        this.worldObj.playAuxSFX(2002, getPosition(), 16428);
        
        setDead();
    }
    
    @Override
    protected float getGravityVelocity() {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }

    @Override
    public ItemStack getItemStack() {
        Optional<ItemStack> optional = dataWatcher.get(ITEMSTACK_INDEX);
        return optional.isPresent() ? optional.get() : null;
    }
    
    private void setItemStack(ItemStack stack) {
        dataWatcher.set(ITEMSTACK_INDEX, Optional.of(stack));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        
        dataWatcher.register(ITEMSTACK_INDEX, Optional.of(WeatherContainer.createItemStack(WeatherContainer.WeatherContainerTypes.EMPTY, 1)));
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }
}
