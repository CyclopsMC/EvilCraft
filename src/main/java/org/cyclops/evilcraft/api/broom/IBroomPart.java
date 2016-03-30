package org.cyclops.evilcraft.api.broom;

import com.google.common.collect.Lists;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;

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
    public String getUnlocalizedName();

    /**
     * The line to be added to tooltips.
     * @param prefix A line prefix
     * @return The line
     */
    public String getTooltipLine(String prefix);

    /**
     * @return The length of this part (1.0F = 1 block)
     */
    public float getLength();

    /**
     * @return The rarity.
     */
    public EnumRarity getRarity();

    /**
     * @return If the item has an effect overlay.
     */
    public boolean isEffect();

    /**
     * All types of broom parts.
     */
    public static class BroomPartType {

        private static final List<BroomPartType> ALL_TYPES = Lists.newArrayList();

        public static final BroomPartType ROD = new BroomPartType("rod", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength) {
                return 0;
            }
        });
        public static final BroomPartType BRUSH = new BroomPartType("brush", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength) {
                return -selfLength;
            }
        });
        public static final BroomPartType CAP = new BroomPartType("cap", new IBroomPartTypeModelOffsetter() {
            @Override
            public float getOffset(float rodLength, float selfLength) {
                return rodLength;
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

        public String getUnlocalizedName() {
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

        public float getOffset(float rodLength, float selfLength);

    }

}
