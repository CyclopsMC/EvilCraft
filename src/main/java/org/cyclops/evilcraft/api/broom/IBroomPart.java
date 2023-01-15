package org.cyclops.evilcraft.api.broom;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import org.cyclops.evilcraft.Reference;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * A broom part.
 * @author rubensworks
 */
public interface IBroomPart {

    /**
     * @return The part type.
     */
    public BroomPartType getType();

    /**
     * @return The unique part identifier.
     */
    public ResourceLocation getId();

    /**
     * @return The unlocalized name.
     */
    public String getTranslationKey();

    /**
     * The line to be added to tooltips.
     * @param prefix A line prefix
     * @return The line, null if no line should be shown
     */
    @Nullable
    public Component getTooltipLine(String prefix);

    /**
     * @return The length of this part (1.0F = 1 block)
     */
    public float getLength();

    /**
     * @return The rarity.
     */
    public Rarity getRarity();

    /**
     * @return If the item has an effect overlay.
     */
    public boolean isEffect();

    /**
     * @return The tint of this part's model.
     */
    public int getModelColor();

    /**
     * @return If an item for this part should be automatically registered if none has been provided.
     */
    public boolean shouldAutoRegisterMissingItem();

    /**
     * All types of broom parts.
     */
    public static class BroomPartType {

        private static final List<BroomPartType> ALL_TYPES = Lists.newArrayList();

        public static final BroomPartType ROD = new BroomPartType("rod", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength, int typeIndex) {
                return 0;
            }
        });
        public static final BroomPartType BRUSH = new BroomPartType("brush", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength, int typeIndex) {
                return -selfLength;
            }
        });
        public static final BroomPartType CAP = new BroomPartType("cap", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength, int typeIndex) {
                return rodLength;
            }
        });
        public static final BroomPartType MODIFIER = new BroomPartType("modifier", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength, int typeIndex) {
                return rodLength - selfLength * (1 + typeIndex);
            }
        });

        private final String name;
        private final IBroomPartTypeModelOffsetter offsetter;

        public static final BroomPartType[] BASE_TYPES = {ROD, BRUSH, CAP};

        public static List<BroomPartType> getAllTypes() {
            return Collections.unmodifiableList(ALL_TYPES);
        }

        public BroomPartType(String name, IBroomPartTypeModelOffsetter offsetter) {
            this.name = name;
            this.offsetter = offsetter;
            ALL_TYPES.add(this);
        }

        public String getName() {
            return name;
        }

        public String getTranslationKey() {
            return "broom.parts." + Reference.MOD_ID + ".type." + getName() + ".name";
        }

        public IBroomPartTypeModelOffsetter getOffsetter() {
            return offsetter;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    public static interface IBroomPartTypeModelOffsetter {

        public float getOffset(float rodLength, float selfLength, int typeIndex);

    }

}
