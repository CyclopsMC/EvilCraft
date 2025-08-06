package org.cyclops.evilcraft.core.broom;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * A broom part for a gem cap
 * @author rubensworks
 */
public class BroomPartCapGem extends BroomPartBase {
    private final int color;

    public BroomPartCapGem(ResourceLocation id, int color) {
        super(id, BroomPartType.CAP, 0.0625F);
        this.color = IModHelpers.get().getBaseHelpers().rgbToBgr(color);
    }

    @Override
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                ResourceLocation.fromNamespaceAndPath(getId().getNamespace(), "broom_part/cap_gem"));
    }

    @Override
    public int getModelColor() {
        return color;
    }
}
