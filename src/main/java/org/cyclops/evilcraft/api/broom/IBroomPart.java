package org.cyclops.evilcraft.api.broom;

import net.minecraft.util.ResourceLocation;

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
     * @return The unique part identifier.
     */
    public ResourceLocation getId();

    /**
     * All types of broom parts.
     */
    public static enum BroomPartType {

        ROD,
        BRUSH,
        CAP

    }

}
