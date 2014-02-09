package evilcraft.entities.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.entities.item.EntityThrowable;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;

public class EntityWeatherContainer extends EntityThrowable implements Configurable {
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    private static final int ITEMSTACK_INDEX = 15;
    
    public EntityWeatherContainer(World world)
    {
        super(world);
    }

    public EntityWeatherContainer(World world, EntityLivingBase entity, int damage)
    {
        this(world, entity, new ItemStack(WeatherContainer.getInstance().itemID, 1, damage));
    }

    public EntityWeatherContainer(World world, EntityLivingBase entity, ItemStack stack)
    {
        super(world, entity);
        setItemStack(stack);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        ItemStack stack = getItemStack();
        WeatherContainerTypes containerType = WeatherContainer.getWeatherContainerType(stack);
        containerType.onUse(worldObj, stack);
        
        if (!worldObj.isRemote) {
            // Play evil sounds at the players in that world
            for(Object o : worldObj.playerEntities) {
                EntityPlayer entityPlayer = (EntityPlayer) o;
                worldObj.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
                worldObj.playSoundAtEntity(entityPlayer, "mob.ghast.moan", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
                worldObj.playSoundAtEntity(entityPlayer, "mob.wither.death", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
            }
        }
        
        // Play sound and show particles of splash potion of harming
        // TODO: make custom particles for this
        this.worldObj.playAuxSFX(2002, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 16428);
        
        setDead();
    }
    
    @Override
    protected float getGravityVelocity()
    {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }

    @Override
    protected float func_70182_d()
    {
        // Determines the distance of the throw
        return 1.0F;
    }

    @Override
    protected float func_70183_g()
    {
        // Offset for the start height at which the entity is thrown
        return 0.0F;
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

    @Override
    public ItemStack getItemStack() {
        return dataWatcher.getWatchableObjectItemStack(ITEMSTACK_INDEX);
    }
    
    private void setItemStack(ItemStack stack) {
        dataWatcher.updateObject(ITEMSTACK_INDEX, stack);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        
        dataWatcher.addObject(ITEMSTACK_INDEX, WeatherContainer.createItemStack(WeatherContainerTypes.EMPTY, 1));
    }
}
