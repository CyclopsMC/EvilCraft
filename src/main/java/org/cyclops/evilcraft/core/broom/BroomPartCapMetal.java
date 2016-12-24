package org.cyclops.evilcraft.core.broom;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * A broom part for a metal cap
 * @author rubensworks
 */
public class BroomPartCapMetal extends BroomPartBase {
    private final int color;

    public BroomPartCapMetal(ResourceLocation id, int color) {
        super(id, BroomPartType.CAP, 0.25F);
        this.color = Helpers.rgbToBgr(color);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getResourceDomain(), "broom_part/cap_metal"));
    }

    @Override
    public int getModelColor() {
        return color;
    }
}
