package org.cyclops.evilcraft.api.broom;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;

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

        public BroomPartType(String name, IBroomPartTypeModelOffsetter offsetter) {
            this.name = name;
            this.offsetter = offsetter;
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
    }

    public static interface IBroomPartTypeModelOffsetter {

        public float getOffset(float rodLength, float selfLength);

    }

}
