package evilcraft.api;


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
    
    
    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;
    public final int flag;
    public static final DirectionCorner[] VALID_DIRECTIONS = {UPPER_NORTH, UPPER_SOUTH, UPPER_WEST, UPPER_EAST, MIDDLE_NORTHWEST, MIDDLE_NORTHEAST, MIDDLE_SOUTHEAST, MIDDLE_SOUTHWEST, LOWER_NORTH, LOWER_SOUTH, LOWER_WEST, LOWER_EAST};
    
    private DirectionCorner(int x, int y, int z)
    {
        offsetX = x;
        offsetY = y;
        offsetZ = z;
        flag = 1 << ordinal();
    }
    
    public static DirectionCorner getOrientation(int id)
    {
        if (id >= 0 && id < VALID_DIRECTIONS.length)
        {
            return VALID_DIRECTIONS[id];
        }
        return UNKNOWN;
    }
}
