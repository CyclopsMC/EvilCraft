package org.cyclops.evilcraft.api.broom;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.entity.item.EntityBroom;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private final List<TextFormatting> tooltipFormats;
    private final int modelColor;
    private final int bakedQuadModelColor;

    private final List<ITickListener> tickListeners = Lists.newLinkedList();
    private final List<ICollisionListener> collisionListeners = Lists.newLinkedList();

    public BroomModifier(ResourceLocation id, Type type, float defaultValue,
                         float tierValue, int maxTiers, boolean baseModifier,
                         List<TextFormatting> tooltipFormats, int modelColor) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
        this.tierValue = tierValue;
        this.maxTiers = maxTiers;
        this.baseModifier = baseModifier;
        this.tooltipFormats = tooltipFormats;
        this.modelColor = modelColor;
        this.bakedQuadModelColor = prepareColor(modelColor, baseModifier);

        if (isBaseModifier()) {
            this.tooltipFormats.add(TextFormatting.ITALIC);
        }
    }

    public BroomModifier(ResourceLocation id, Type type, float defaultValue,
                         float tierValue, int maxTiers, boolean baseModifier,
                         TextFormatting singleFormat, int modelColor) {
        this(id, type, defaultValue, tierValue, maxTiers, baseModifier, Lists.newArrayList(singleFormat), modelColor);
    }

    protected static int prepareColor(int modelColor, boolean baseModifier) {
        return Helpers.rgbToBgra(modelColor, baseModifier ? 255 : 200);
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getName() {
        return id.getPath();
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

    public String getTranslationKey() {
        return "broom.modifiers." + id.getNamespace() + ".type." + getName() + ".name";
    }

    public boolean showTooltip() {
        return this != BroomModifiers.MODIFIER_COUNT;
    }

    public ITextComponent getTooltipLine(String prefix, float value, float bonusValue) {
        return getTooltipLine(prefix, value, bonusValue, true);
    }

    public ITextComponent getTooltipLine(String prefix, float value, float bonusValue, boolean showMaxValue) {
        ITextComponent suffix = new TranslationTextComponent(getTranslationKey())
                .appendText(": " + value);
        if (bonusValue > 0) {
            suffix = suffix.appendText(String.format(" (+%s)", bonusValue));
        }
        if (showMaxValue) {
            suffix = suffix.appendText(String.format(" / %s", getMaxTierValue()));
        }

        ITextComponent ret = new StringTextComponent(prefix)
                .appendSibling(suffix);
        for (TextFormatting format : getTooltipFormats()) {
            ret = ret.applyTextStyle(format);
        }
        return ret;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isBaseModifier() {
        return baseModifier;
    }

    public List<TextFormatting> getTooltipFormats() {
        return tooltipFormats;
    }

    public int getModelColor() {
        return modelColor;
    }

    public int getBakedQuadModelColor() {
        return bakedQuadModelColor;
    }

    public void addTickListener(ITickListener listener) {
        this.tickListeners.add(listener);
    }

    public void addCollisionListener(ICollisionListener listener) {
        this.collisionListeners.add(listener);
    }

    public List<ITickListener> getTickListeners() {
        return Collections.unmodifiableList(tickListeners);
    }

    public List<ICollisionListener> getCollisionListeners() {
        return Collections.unmodifiableList(collisionListeners);
    }

    /**
     * Get the tier for the given modifier value.
     * @param modifier The modifier type.
     * @param value The modifier value.
     * @return The tier.
     */
    public static int getTier(BroomModifier modifier, float value) {
        return (int) Math.ceil(value / modifier.getTierValue());
    }

    /**
     * Calculate a weighted average color of the given modifiers.
     * @param modifiers The modifiers.
     * @return The weighted average color.
     */
    public static Triple<Float, Float, Float> getAverageColor(Map<BroomModifier, Float> modifiers) {
        float r = 0;
        float g = 0;
        float b = 0;
        float count = 0;
        for (Map.Entry<BroomModifier, Float> entry : modifiers.entrySet()) {
            BroomModifier modifier = entry.getKey();
            Triple<Float, Float, Float> color = Helpers.intToRGB(modifier.getModelColor());
            float factor = entry.getValue() / modifier.getMaxTierValue();
            r += color.getLeft() * factor;
            g += color.getMiddle() * factor;
            b += color.getRight() * factor;
            count += factor;
        }
        return Triple.of(r / count, g / count, b / count);
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

    public static interface ITickListener {
        public void onTick(EntityBroom broom, float modifierValue);
    }
    public static interface ICollisionListener {
        public void onCollide(EntityBroom broom, Entity entity, float modifierValue);
    }

}
