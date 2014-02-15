package evilcraft.api;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * An equivalent to {@link ForgeDirection} for being able to also refer to
 * block positions that are at a euclidian distance of maximum 2.
 * @see ForgeDirection
 * @author rubensworks
 *
 */
public enum DirectionCorner{
    
    /** -Z;+Y */
    UPPER_NORTH(0, 1, -1),

    /** +Z;+Y */
    UPPER_SOUTH(0, 1, 1),

    /** -X;+Y */
    UPPER_WEST(-1, 1, 0),

    /** +X;+Y */
    UPPER_EAST(1, 1, 0),
    
    /** -Z;+Y */
    MIDDLE_NORTHWEST(-1, 0, -1),

    /** +Z;+Y */
    MIDDLE_NORTHEAST(1, 0, -1),

    /** -X;+Y */
    MIDDLE_SOUTHEAST(1, 0, 1),

    /** +X;+Y */
    MIDDLE_SOUTHWEST(-1, 0, 1),
    
    /** -Z;-Y */
    LOWER_NORTH(0, -1, -1),

    /** +Z;-Y */
    LOWER_SOUTH(0, -1, 1),

    /** -X;-Y */
    LOWER_WEST(-1, -1, 0),

    /** +X;-Y */
    LOWER_EAST(1, -1, 0),
    
    /**
     * Used only by getOrientation, for invalid inputs
     */
    UNKNOWN(0, 0, 0);
    
    /**
     * The offset in the X axis.
     */
    public final int offsetX;
    /**
     * The offset in the Y axis.
     */
    public final int offsetY;
    /**
     * The offset in the Z axis.
     */
    public final int offsetZ;
    /**
     * The bitwise identifier for this direction, used for bitwise toggling of directions.
     */
    public final int flag;
    
    /**
     * All the valid directions.
     */
    public static final DirectionCorner[] VALID_DIRECTIONS = {
        UPPER_NORTH, UPPER_SOUTH, UPPER_WEST, UPPER_EAST,
        MIDDLE_NORTHWEST, MIDDLE_NORTHEAST, MIDDLE_SOUTHEAST, MIDDLE_SOUTHWEST,
        LOWER_NORTH, LOWER_SOUTH, LOWER_WEST, LOWER_EAST
        };
    
    private DirectionCorner(int x, int y, int z)
    {
        offsetX = x;
        offsetY = y;
        offsetZ = z;
        flag = 1 << ordinal();
    }
    
    /**
     * Get the direction of the given value, inverse of the @see DirectionCorner#ordinal() method.
     * @param id The ordinal value of a direction.
     * @return The direction for the given ordinal value.
     */
    public static DirectionCorner getOrientation(int id)
    {
        if (id >= 0 && id < VALID_DIRECTIONS.length)
        {
            return VALID_DIRECTIONS[id];
        }
        return UNKNOWN;
    }
}
