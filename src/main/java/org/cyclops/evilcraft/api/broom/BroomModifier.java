package org.cyclops.evilcraft.api.broom;

import com.google.common.base.Function;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Modifier for brooms
 * @author rubensworks
 */
public class BroomModifier {

    private final ResourceLocation id;
    private final Type type;
    private final float defaultValue;
    private final float tierValue;
    private final int maxTiers;
    private final boolean baseModifier;

    public BroomModifier(ResourceLocation id, Type type, float defaultValue,
                         float tierValue, int maxTiers, boolean baseModifier) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
        this.tierValue = tierValue;
        this.maxTiers = maxTiers;
        this.baseModifier = baseModifier;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getName() {
        return id.getResourcePath();
    }

    public Type getType() {
        return type;
    }

    public float apply(float baseValue, List<Float> values) {
        float value = baseValue;
        for (Float v : values) {
            value = getType().applyer.apply(Pair.of(value, v));
        }
        return value;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getTierValue() {
        return tierValue;
    }

    public int getMaxTiers() {
        return maxTiers;
    }

    public float getMaxTierValue() {
        return getTierValue() * getMaxTiers();
    }

    public String getUnlocalizedName() {
        return "broom.modifiers." + id.getResourceDomain() + ".type." + getName() + ".name";
    }

    public String getTooltipLine(String prefix, float value, float bonusValue) {
        String suffix;
        if(bonusValue > 0) {
            suffix = String.format("%s: %s (+%s) / %s", L10NHelpers.localize(getUnlocalizedName()),
                    value, bonusValue, getMaxTierValue());
        } else {
            suffix = String.format("%s: %s / %s", L10NHelpers.localize(getUnlocalizedName()),
                    value, getMaxTierValue());
        }
        return L10NHelpers.localize(prefix + suffix);
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isBaseModifier() {
        return baseModifier;
    }

    public static enum Type {

        ADDITIVE(new Function<Pair<Float, Float>, Float>() {
            @Nullable
            @Override
            public Float apply(Pair<Float, Float> input) {
                return input.getLeft() + input.getRight();
            }
        }),
        MULTIPLICATIVE(new Function<Pair<Float, Float>, Float>() {
            @Nullable
            @Override
            public Float apply(@Nullable Pair<Float, Float> input) {
                return input.getLeft() * input.getRight();
            }
        });

        private final Function<Pair<Float, Float>, Float> applyer;

        private Type(Function<Pair<Float, Float>, Float> applyer) {
            this.applyer = applyer;
        }

        public Function<Pair<Float, Float>, Float> getApplyer() {
            return applyer;
        }
    }

}
