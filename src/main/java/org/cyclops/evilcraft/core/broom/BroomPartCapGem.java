package org.cyclops.evilcraft.core.broom;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.ModelHelpers;

/**
 * A broom part for a gem cap
 * @author rubensworks
 */
public class BroomPartCapGem extends BroomPartBase {
    private final int color;

    public BroomPartCapGem(ResourceLocation id, int color) {
        super(id, BroomPartType.CAP, 0.0625F);
        this.color = ModelHelpers.rgbToBgr(color);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getResourceDomain(), "broomPart/cap_gem"));
    }

    @Override
    public int getModelColor() {
        return color;
    }
}
