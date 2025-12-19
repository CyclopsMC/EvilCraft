package org.cyclops.evilcraft.core.broom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Rarity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Base implementation for broom parts.
 *
 * @author rubensworks
 */
public class BroomPartBase implements IBroomPart {

    private final Identifier id;
    private final BroomPartType type;
    private final float length;
    private final Rarity rarity;
    private final boolean effect;

    public BroomPartBase(Identifier id, BroomPartType type, float length) {
        this(id, type, length, Rarity.COMMON, false);
    }

    public BroomPartBase(Identifier id, BroomPartType type, float length, Rarity rarity, boolean effect) {
        this.id = id;
        this.type = type;
        this.length = length;
        this.rarity = rarity;
        this.effect = effect;
        registerModelIdentifier();
    }

    public Identifier getId() {
        return this.id;
    }

    protected void registerModelIdentifier() {
        BroomParts.REGISTRY.registerPartModel(this,
                Identifier.fromNamespaceAndPath(getId().getNamespace(), "broom_part/" + getId().getPath().toLowerCase(Locale.ROOT)));
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
        return IModHelpers.get().getBaseHelpers().RGBAToInt(255, 255, 255, 255);
    }

    public BroomPartType getType() {
        return this.type;
    }

    public float getLength() {
        return this.length;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public boolean isEffect() {
        return this.effect;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BroomPartBase)) return false;
        final BroomPartBase other = (BroomPartBase) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        if (Float.compare(this.getLength(), other.getLength()) != 0) return false;
        final Object this$rarity = this.getRarity();
        final Object other$rarity = other.getRarity();
        if (this$rarity == null ? other$rarity != null : !this$rarity.equals(other$rarity)) return false;
        if (this.isEffect() != other.isEffect()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BroomPartBase;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + Float.floatToIntBits(this.getLength());
        final Object $rarity = this.getRarity();
        result = result * PRIME + ($rarity == null ? 43 : $rarity.hashCode());
        result = result * PRIME + (this.isEffect() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "BroomPartBase(id=" + this.getId() + ", type=" + this.getType() + ", length=" + this.getLength() + ", rarity=" + this.getRarity() + ", effect=" + this.isEffect() + ")";
    }
}
