package org.cyclops.evilcraft.api.broom;

/**
 * A broom part.
 * @author rubensworks
 */
public interface IBroomPart {

    /**
     * @return The part type.
     */
    public BroomPartType getType();

    /**
     * All types of broom parts.
     */
    public static enum BroomPartType {

        ROD,
        BRUSH,
        CAP

    }

}
