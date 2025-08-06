package org.cyclops.evilcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;

import java.util.function.Consumer;

/**
 * Promise item singleton.
 * Used for machine upgrades.
 *
 * @author rubensworks
 *
 */
public class ItemPromise extends Item {

    private final Upgrades.Upgrade upgrade;

    public ItemPromise(Properties properties, Upgrades.Upgrade upgrade) {
        super(properties);
        this.upgrade = upgrade;
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        if(upgrade.getTier() > 0) { // All the 'tier' upgrades can only have StackSize 1.
            return 1;
        }
        return super.getMaxStackSize(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltipDisplay, tooltipAdder, flag);
        if(IModHelpers.get().getMinecraftClientHelpers().isShifted()) {
            tooltipAdder.accept(Component.translatable("item.evilcraft.promise.use_in")
                    .withStyle(ChatFormatting.DARK_GREEN));
            for(BlockConfigCommon upgradable : getUpgrade(itemStack).getUpgradables()) {
                tooltipAdder.accept(Component.translatable(upgradable.getTranslationKey())
                        .withStyle(ChatFormatting.ITALIC));
            }
        }
    }

    /**
     * Get the upgrade for given damage.
     * @param itemStack The item.
     * @return The upgrade instance.
     */
    public Upgrades.Upgrade getUpgrade(ItemStack itemStack) {
        return upgrade;
    }

    public static Item getItem(int tier) {
        if (tier == 1) {
            return getItem(Upgrades.UPGRADE_TIER1);
        } else if (tier == 2) {
            return getItem(Upgrades.UPGRADE_TIER2);
        } else if (tier == 3) {
            return getItem(Upgrades.UPGRADE_TIER3);
        } else {
            throw new IllegalStateException("Could not find an item for tier " + tier);
        }
    }

    public static Item getItem(Upgrades.Upgrade upgrade) {
        if (upgrade == Upgrades.UPGRADE_TIER1) {
            return RegistryEntries.ITEM_PROMISE_TIER_1.get();
        } else if (upgrade == Upgrades.UPGRADE_TIER2) {
            return RegistryEntries.ITEM_PROMISE_TIER_2.get();
        } else if (upgrade == Upgrades.UPGRADE_TIER3) {
            return RegistryEntries.ITEM_PROMISE_TIER_3.get();
        } else if (upgrade == Upgrades.UPGRADE_SPEED) {
            return RegistryEntries.ITEM_PROMISE_SPEED.get();
        } else if (upgrade == Upgrades.UPGRADE_EFFICIENCY) {
            return RegistryEntries.ITEM_PROMISE_EFFICIENCY.get();
        } else {
            throw new IllegalStateException("Could not find an item for " + upgrade);
        }
    }

    /**
     * @param itemStack The item.
     * @return If the upgrade is a tier upgrade.
     */
    public boolean isTierUpgrade(ItemStack itemStack) {
        return !itemStack.isEmpty() && upgrade.getTier() > 0;
    }

}
