package org.cyclops.evilcraft.core.broom;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * A broom part for a metal cap
 * @author rubensworks
 */
public class BroomPartCapMetal extends BroomPartBase {
    private final int color;

    public BroomPartCapMetal(ResourceLocation id, int color) {
        super(id, BroomPartType.CAP, 0.25F);
        this.color = IModHelpers.get().getBaseHelpers().rgbToBgr(color);
    }

    @Override
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "broom_part/cap_metal"));
    }

    @Override
    public int getModelColor() {
        return color;
    }
}
