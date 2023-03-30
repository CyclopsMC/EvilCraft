package org.cyclops.evilcraft.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Glasses that make you see spirits.
 * @author rubensworks
 *
 */
public class ItemSpectralGlasses extends ArmorItem {

    public ItemSpectralGlasses(Properties properties) {
        super(new Material(), Type.HELMET, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        ItemStack existingStack = playerIn.getItemBySlot(getEquipmentSlot());
        if (existingStack.isEmpty()) {
            playerIn.setItemSlot(getEquipmentSlot(), itemStackIn.copy());
            itemStackIn.shrink(1);
            return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, itemStackIn);
        }
        return super.use(worldIn, playerIn, hand);
    }

    public static class Material implements ArmorMaterial {

        private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

        @Override
        public int getDurabilityForType(Type slotIn) {
            return MAX_DAMAGE_ARRAY[slotIn.getSlot().getIndex()] * 15;
        }

        @Override
        public int getDefenseForType(Type slotIn) {
            return new int[]{1, 4, 5, 2}[slotIn.getSlot().getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RegistryEntries.ITEM_DARK_GEM_CRUSHED);
        }

        @Override
        public String getName() {
            return Reference.MOD_ID + ":" + "spectral_glasses";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }
}
