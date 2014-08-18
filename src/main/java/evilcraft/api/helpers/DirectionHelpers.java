package evilcraft.api.helpers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import evilcraft.api.DirectionCorner;

/**
 * This class contains helper methods involving directions.
 * 
 * @author immortaleeb
 *
 */
public class DirectionHelpers {
	/**
     * A list of all the {@link ForgeDirection}.
     */
    public static List<ForgeDirection> DIRECTIONS = Arrays.asList(ForgeDirection.VALID_DIRECTIONS);
    /**
     * A list of all the {@link DirectionCorner}
     */
    public static List<DirectionCorner> DIRECTIONS_CORNERS = Arrays.asList(DirectionCorner.VALID_DIRECTIONS);
    /**
     * The facing directions of an entity, used in {@link Helpers#getEntityFacingDirection(EntityLivingBase)}.
     */
    public static final ForgeDirection[] ENTITYFACING =
        {ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST};
    /**
     * A double array that contains the visual side. The first argument should be the rotation of
     * the block and the second argument is the side for which the texture is called.
     */
    public static ForgeDirection[][] TEXTURESIDE_ORIENTATION = {
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST}, // DOWN
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST}, // UP
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST}, // NORTH
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST}, // SOUTH
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH}, // WEST
        {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH}, // EAST
    };
    
    /**
     * Get an iterator for all the {@link ForgeDirection}.
     * @return The {@link ForgeDirection} iterator
     * @see Helpers#DIRECTIONS
     * @see ForgeDirection
     */
    public static Iterator<ForgeDirection> getDirectionIterator() {
        return DIRECTIONS.iterator();
    }
    
    /**
     * Get the ForgeDirection the entity is facing, only vertical directions.
     * @param entity The entity that is facing a direction.
     * @return The {@link ForgeDirection} the entity is facing.
     */
    public static ForgeDirection getEntityFacingDirection(EntityLivingBase entity) {
        int facingDirection = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
        return ENTITYFACING[facingDirection];
    }
    
    /**
     * Get the {@link ForgeDirection} from the sign of an X offset.
     * @param xSign X offset from somewhere.
     * @return The {@link ForgeDirection} for the offset.
     */
    public static ForgeDirection getForgeDirectionFromXSign(int xSign) {
    	return xSign > 0 ? ForgeDirection.EAST : ForgeDirection.WEST;
    }
    
    /**
     * Get the {@link ForgeDirection} from the sign of an Z offset.
     * @param zSign Z offset from somewhere.
     * @return The {@link ForgeDirection} for the offset.
     */
    public static ForgeDirection getForgeDirectionFromZSing(int zSign) {
    	return zSign > 0 ? ForgeDirection.SOUTH : ForgeDirection.NORTH;
    }
}
