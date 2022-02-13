package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import java.util.List;

/**
 * Helpers for items.
 * @author rubensworks
 *
 */
public class ItemPowerableHelpers {

    private static final String NBT_KEY_POWER = "power";

    /**
     * Get the power level of the given ItemStack.
     * @param itemStack The item to check.
     * @return The power this Mace currently has.
     */
    public static int getPower(ItemStack itemStack) {
        return ItemHelpers.getNBTInt(itemStack, NBT_KEY_POWER);
    }

    /**
     * Set the power level of the given ItemStack.
     * @param itemStack The item to change.
     * @param power The new power level.
     */
    public static void setPower(ItemStack itemStack, int power) {
        ItemHelpers.setNBTInt(itemStack, power, NBT_KEY_POWER);
    }

    /**
     * Should be called by powerable items on right click.
     * @param itemStack The item stack, this will possibly have an updated power level.
     * @param world The world.
     * @param player The player
     * @param powerLevels The maximum power levels.
     * @param onSneak If the action should be executed for a sneaking player.
     * @return If the power level was changed.
     */
    public static boolean onPowerableItemItemRightClick(ItemStack itemStack, World world, PlayerEntity player, int powerLevels, boolean onSneak) {
        if(onSneak == player.isCrouching()) {
            if(!world.isClientSide()) {
                int newPower = (getPower(itemStack) + 1) % powerLevels;
                setPower(itemStack, newPower);
                player.displayClientMessage(new TranslationTextComponent("item." + Reference.MOD_ID + ".powerable.set_power", newPower)
                        .withStyle(TextFormatting.DARK_PURPLE), true);
            }
            return true;
        }
        return false;
    }

    /**
     * Add the power to the item information, should be called before super.
     * @param itemStack The itemstack with a power
     * @param lines The lines to add the information to.
     */
    public static void addPreInformation(ItemStack itemStack, List<ITextComponent> lines) {
        L10NHelpers.addOptionalInfo(lines, "item." + Reference.MOD_ID + ".powerable");
    }

    /**
     * Add the power to the item information, should be called after super.
     * @param itemStack The itemstack with a power
     * @param lines The lines to add the information to.
     */
    public static void addPostInformation(ItemStack itemStack, List<ITextComponent> lines) {
        lines.add(new TranslationTextComponent("item." + Reference.MOD_ID + ".powerable.info.power", getPower(itemStack))
                .withStyle(TextFormatting.BOLD));
    }
	
}
