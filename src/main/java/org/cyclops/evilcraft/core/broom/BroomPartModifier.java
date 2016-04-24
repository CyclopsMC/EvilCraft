package org.cyclops.evilcraft.core.broom;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.evilcraft.api.broom.BroomModifier;

import javax.annotation.Nullable;

/**
 * A broom part for a modifier
 * @author rubensworks
 */
public class BroomPartModifier extends BroomPartBase {
    private final BroomModifier modifier;

    public BroomPartModifier(BroomModifier modifier) {
        super(modifier.getId(), BroomPartType.MODIFIER, 0.0625F);
        this.modifier = modifier;
    }

    @SideOnly(Side.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getResourceDomain(), "broomPart/ring"));
    }

    @Override
    public @Nullable String getTooltipLine(String prefix) {
        return null;
    }

    @Override
    public boolean shouldAutoRegisterMissingItem() {
        return false;
    }

    @Override
    public int getModelColor() {
        return modifier.getModelColor();
    }
}
