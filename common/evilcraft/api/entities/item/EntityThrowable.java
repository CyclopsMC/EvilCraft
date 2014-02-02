package evilcraft.api.entities.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class EntityThrowable extends net.minecraft.entity.projectile.EntityThrowable {
    
    public EntityThrowable(World world)
    {
        super(world);
    }

    public EntityThrowable(World world, EntityLivingBase entity)
    {
        super(world, entity);
    }

    public EntityThrowable(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }
    
   /**
    * returns an item stack with the item damage and the throwable item 
    * associated with this entity
    */
    public abstract ItemStack getItemStack();
}
