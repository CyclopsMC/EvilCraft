package org.cyclops.evilcraft.core.broom;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * A broom part for a gem cap
 * @author rubensworks
 */
public class BroomPartCapGem extends BroomPartBase {
    private final int color;

    public BroomPartCapGem(ResourceLocation id, int color) {
        super(id, BroomPartType.CAP, 0.0625F);
        this.color = Helpers.rgbToBgr(color);
    }

    @OnlyIn(Dist.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getNamespace(), "broom_part/cap_gem"));
    }

    @Override
    public int getModelColor() {
        return color;
    }
}
