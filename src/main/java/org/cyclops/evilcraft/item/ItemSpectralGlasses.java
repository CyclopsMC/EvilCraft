package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Glasses that make you see spirits.
 * @author rubensworks
 *
 */
public class ItemSpectralGlasses extends ArmorItem {

    public ItemSpectralGlasses(Properties properties) {
        super(new Material(), EquipmentSlotType.HEAD, properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        ItemStack existingStack = playerIn.getItemStackFromSlot(getEquipmentSlot());
        if (existingStack.isEmpty()) {
            playerIn.setItemStackToSlot(getEquipmentSlot(), itemStackIn.copy());
            itemStackIn.shrink(1);
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    public static class Material implements IArmorMaterial {

        private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * 15;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return new int[]{1, 4, 5, 2}[slotIn.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(RegistryEntries.ITEM_DARK_GEM_CRUSHED);
        }

        @Override
        public String getName() {
            return Reference.MOD_ID + ":" + "spectral_glasses";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }
    }
}
