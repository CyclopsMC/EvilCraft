package org.cyclops.evilcraft.item;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;

import java.util.List;
import java.util.Map;

/**
 * Promise item singleton.
 * Used for machine upgrades.
 *
 * @author rubensworks
 *
 */
public class ItemPromise extends Item {

    public static final Upgrades.Upgrade[] UPGRADES = new Upgrades.Upgrade[]{
            Upgrades.UPGRADE_TIER1,
            Upgrades.UPGRADE_TIER2,
            Upgrades.UPGRADE_TIER3,
            Upgrades.UPGRADE_SPEED,
            Upgrades.UPGRADE_EFFICIENCY
    };
    public static final Map<Upgrades.Upgrade, Integer> MAIN_COLORS = Maps.newHashMap();
    public static final Map<Upgrades.Upgrade, Integer> SECONDARY_COLORS = Maps.newHashMap();
    static {
        MAIN_COLORS.put(Upgrades.UPGRADE_TIER1, Helpers.RGBToInt(220, 220, 220));
        SECONDARY_COLORS.put(Upgrades.UPGRADE_TIER1, Helpers.RGBToInt(255, 255, 255));

        MAIN_COLORS.put(Upgrades.UPGRADE_TIER2, Helpers.RGBToInt(234, 238, 87));
        SECONDARY_COLORS.put(Upgrades.UPGRADE_TIER2, Helpers.RGBToInt(230, 230, 160));

        MAIN_COLORS.put(Upgrades.UPGRADE_TIER3, Helpers.RGBToInt(51, 235, 203));
        SECONDARY_COLORS.put(Upgrades.UPGRADE_TIER3, Helpers.RGBToInt(150, 250, 200));

        MAIN_COLORS.put(Upgrades.UPGRADE_SPEED, Helpers.RGBToInt(200, 90, 80));
        SECONDARY_COLORS.put(Upgrades.UPGRADE_SPEED, Helpers.RGBToInt(240, 120, 110));

        MAIN_COLORS.put(Upgrades.UPGRADE_EFFICIENCY, Helpers.RGBToInt(80, 70, 200));
        SECONDARY_COLORS.put(Upgrades.UPGRADE_EFFICIENCY, Helpers.RGBToInt(120, 120, 210));
    }

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
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        if(MinecraftHelpers.isShifted()) {
            list.add(Component.translatable("item.evilcraft.promise.use_in")
                    .withStyle(ChatFormatting.DARK_GREEN));
            for(BlockConfig upgradable : getUpgrade(itemStack).getUpgradables()) {
                list.add(Component.translatable(upgradable.getTranslationKey())
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
            return RegistryEntries.ITEM_PROMISE_TIER_1;
        } else if (upgrade == Upgrades.UPGRADE_TIER2) {
            return RegistryEntries.ITEM_PROMISE_TIER_2;
        } else if (upgrade == Upgrades.UPGRADE_TIER3) {
            return RegistryEntries.ITEM_PROMISE_TIER_3;
        } else if (upgrade == Upgrades.UPGRADE_SPEED) {
            return RegistryEntries.ITEM_PROMISE_SPEED;
        } else if (upgrade == Upgrades.UPGRADE_EFFICIENCY) {
            return RegistryEntries.ITEM_PROMISE_EFFICIENCY;
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

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return upgrade.getTier() > 0 ? Rarity.RARE : Rarity.UNCOMMON;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            Upgrades.Upgrade upgrade = ((ItemPromise) itemStack.getItem()).getUpgrade(itemStack);
            return renderPass == 0 ? SECONDARY_COLORS.get(upgrade) : MAIN_COLORS.get(upgrade);
        }
    }

}
