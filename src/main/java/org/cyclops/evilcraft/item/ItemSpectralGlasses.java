package org.cyclops.evilcraft.item;

import net.minecraft.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.Reference;

import java.util.EnumMap;

/**
 * Glasses that make you see spirits.
 * @author rubensworks
 *
 */
public class ItemSpectralGlasses extends Item {

    public static final ArmorMaterial MATERIAL = new ArmorMaterial(25, Util.make(new EnumMap<>(ArmorType.class), p_371202_ -> {
        p_371202_.put(ArmorType.BOOTS, 1);
        p_371202_.put(ArmorType.LEGGINGS, 2);
        p_371202_.put(ArmorType.CHESTPLATE, 3);
        p_371202_.put(ArmorType.HELMET, 1);
        p_371202_.put(ArmorType.BODY, 3);
    }), 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "repairs_spectral_glasses")),
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "spectral")));

    public ItemSpectralGlasses(Properties properties) {
        super(properties.humanoidArmor(MATERIAL, ArmorType.HELMET));
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        ItemStack existingStack = playerIn.getItemBySlot(getEquipmentSlot(itemStackIn));
        if (existingStack.isEmpty()) {
            playerIn.setItemSlot(getEquipmentSlot(itemStackIn), itemStackIn.copy());
            itemStackIn.shrink(1);
            return InteractionResult.SUCCESS.heldItemTransformedTo(itemStackIn);
        }
        return super.use(worldIn, playerIn, hand);
    }
}
