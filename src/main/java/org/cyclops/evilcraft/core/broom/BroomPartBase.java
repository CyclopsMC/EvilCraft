package org.cyclops.evilcraft.core.broom;

import lombok.Data;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;

import javax.annotation.Nullable;

/**
 * Base implementation for broom parts.
 * @author rubensworks
 */
@Data
public class BroomPartBase implements IBroomPart {

    private final ResourceLocation id;
    private final BroomPartType type;
    private final float length;
    private final EnumRarity rarity;
    private final boolean effect;

    public BroomPartBase(ResourceLocation id, BroomPartType type, float length) {
        this(id, type, length, EnumRarity.COMMON, false);
    }

    public BroomPartBase(ResourceLocation id, BroomPartType type, float length, EnumRarity rarity, boolean effect) {
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

    @SideOnly(Side.CLIENT)
    protected void registerModelResourceLocation() {
        BroomParts.REGISTRY.registerPartModel(this,
                new ResourceLocation(getId().getNamespace(), "broom_part/" + getId().getPath().toLowerCase()));
    }

    @Override
    public String getTranslationKey() {
        return "broom.parts." + getId().getNamespace() + "." + getId().getPath();
    }

    @Override
    public @Nullable String getTooltipLine(String prefix) {
        return prefix + L10NHelpers.localize(getTranslationKey() + ".name");
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
