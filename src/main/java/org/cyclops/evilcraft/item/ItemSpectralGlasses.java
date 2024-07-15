package org.cyclops.evilcraft.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Glasses that make you see spirits.
 * @author rubensworks
 *
 */
public class ItemSpectralGlasses extends ArmorItem {

    public ItemSpectralGlasses(Properties properties) {
        super(RegistryEntries.ARMORMATERIAL_SPECTRAL_GLASSES, Type.HELMET, properties);
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
}
