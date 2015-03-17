package evilcraft.core.helper;

import evilcraft.core.DirectionCorner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains helper methods involving directions.
 * 
 * @author immortaleeb
 *
 */
public class DirectionHelpers {
	/**
     * A list of all the {@link EnumFacing}.
     */
    public static List<EnumFacing> DIRECTIONS = Arrays.asList(EnumFacing.VALUES);
    /**
     * A list of all the {@link DirectionCorner}
     */
    public static List<DirectionCorner> DIRECTIONS_CORNERS = Arrays.asList(DirectionCorner.VALID_DIRECTIONS);
    /**
     * The facing directions of an entity, used in {@link DirectionHelpers#getEntityFacingDirection(EntityLivingBase)}.
     */
    public static final EnumFacing[] ENTITYFACING = EnumFacing.HORIZONTALS;
    /**
     * A double array that contains the visual side. The first argument should be the rotation of
     * the blockState and the second argument is the side for which the texture is called.
     */
    public static EnumFacing[][] TEXTURESIDE_ORIENTATION = {
        {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // DOWN
        {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // UP
        {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, // NORTH
        {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST}, // SOUTH
        {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, // WEST
        {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH}, // EAST
    };
    
    /**
     * Get an iterator for all the {@link EnumFacing}.
     * @return The {@link EnumFacing} iterator
     * @see DirectionHelpers#DIRECTIONS
     * @see EnumFacing
     */
    public static Iterator<EnumFacing> getDirectionIterator() {
        return DIRECTIONS.iterator();
    }
    
    /**
     * Get the EnumFacing the entity is facing, only vertical directions.
     * @param entity The entity that is facing a direction.
     * @return The {@link EnumFacing} the entity is facing.
     */
    public static EnumFacing getEntityFacingDirection(EntityLivingBase entity) {
        int facingDirection = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
        return ENTITYFACING[facingDirection];
    }
    
    /**
     * Get the {@link EnumFacing} from the sign of an X offset.
     * @param xSign X offset from somewhere.
     * @return The {@link EnumFacing} for the offset.
     */
    public static EnumFacing getEnumFacingFromXSign(int xSign) {
    	return xSign > 0 ? EnumFacing.EAST : EnumFacing.WEST;
    }
    
    /**
     * Get the {@link EnumFacing} from the sign of an Z offset.
     * @param zSign Z offset from somewhere.
     * @return The {@link EnumFacing} for the offset.
     */
    public static EnumFacing getEnumFacingFromZSing(int zSign) {
    	return zSign > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
    }
}
