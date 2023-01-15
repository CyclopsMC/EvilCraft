package org.cyclops.evilcraft.core.broom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    @OnlyIn(Dist.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getNamespace(), "broom_part/ring"));
    }

    @Nullable
    @Override
    public Component getTooltipLine(String prefix) {
        return null;
    }

    @Override
    public boolean shouldAutoRegisterMissingItem() {
        return false;
    }

    @Override
    public int getModelColor() {
        return modifier.getBakedQuadModelColor();
    }
}
