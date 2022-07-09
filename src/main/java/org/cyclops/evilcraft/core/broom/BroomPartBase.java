package org.cyclops.evilcraft.core.broom;

import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Base implementation for broom parts.
 * @author rubensworks
 */
@Data
public class BroomPartBase implements IBroomPart {

    private final ResourceLocation id;
    private final BroomPartType type;
    private final float length;
    private final Rarity rarity;
    private final boolean effect;

    public BroomPartBase(ResourceLocation id, BroomPartType type, float length) {
        this(id, type, length, Rarity.COMMON, false);
    }

    public BroomPartBase(ResourceLocation id, BroomPartType type, float length, Rarity rarity, boolean effect) {
        this.id = id;
        this.type = type;
        this.length = length;
        this.rarity = rarity;
        this.effect = effect;
        if(MinecraftHelpers.isClientSide()) {
            registerModelResourceLocation();
        }
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @OnlyIn(Dist.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getNamespace(), "broom_part/" + getId().getPath().toLowerCase(Locale.ROOT)));
    }

    @Override
    public String getTranslationKey() {
        return "broom.parts." + getId().getNamespace() + "." + getId().getPath();
    }

    @Nullable
    @Override
    public Component getTooltipLine(String prefix) {
        return Component.literal(prefix)
                .append(Component.translatable(getTranslationKey()));
    }

    @Override
    public boolean shouldAutoRegisterMissingItem() {
        return true;
    }

    @Override
    public int getModelColor() {
        return Helpers.RGBAToInt(255, 255, 255, 255);
    }
}
