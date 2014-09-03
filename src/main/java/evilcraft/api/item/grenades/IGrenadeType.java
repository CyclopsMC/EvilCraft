package evilcraft.api.item.grenades;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import evilcraft.entities.item.EntityGrenade;
import evilcraft.items.Grenade;

/**
 * Interface for all grenade types. Classes that inherit from this class implement
 * the behavior of the different types of effects that can be put on a {@link Grenade}.
 * @author immortaleeb
 *
 */
public interface IGrenadeType {
	/**
	 * This function is called when a grenade that contains the current grenade type
	 * impacts.
	 * 
	 * @param world The world object.
	 * @param thrower The entity that threw the grenade.
	 * @param grenade The actual grenade entity that was thrown.
	 * @param pos The position where the entity collided.
	 * @param random A random generator, might be usefull to do random stuff.
	 * @return In case this function returns true, all other effects that follow the current
	 *         grenade type will not be executed. If you want all further effects in the chain
	 *         to be executed, return false.
	 */
    public boolean onImpact(
            World world, EntityLivingBase thrower, EntityGrenade grenade,
            MovingObjectPosition pos, Random random);
    
    /**
     * Returns the texture name (e.g. evilcraft:lightningGrenade) for the texture that represents
     * the current grenade type. 
     * @return The texture name for the current grenade type.
     */
    public String getTextureName();
    
    /**
     * @return Returns the location of the texture of this grenade type.
     */
    public ResourceLocation getTextureLocation();

    /**
     * Returns a unique numeric id for the current grenade type.
     * @return The unique numeric of the current grenade type.
     */
    public int getId();
    
    /**
     * Hook to add information to the tooltip shown when a player hovers over a
     * grenade that contains this grenade type.
     * @param list Add strings to this list to add tooltip information.
     */
    public void addInformation(List list);
    
    /**
     * @return Returns the icon for this grenade type.
     */
    public IIcon getIcon();
    
    /**
     * Any icons that need to be registered should be registered in this callback method.
     * This method is called once, when the icons for grenades are registered, so make sure
     * that the icons are stored in a place that is accessible in the future by all instances
     * of this type.
     * @param iconRegister An icon register that can be used to register the icons.
     */
    public void registerIcons(IIconRegister iconRegister);
}
