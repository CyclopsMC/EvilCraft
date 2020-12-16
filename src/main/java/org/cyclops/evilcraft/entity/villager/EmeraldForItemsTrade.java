package org.cyclops.evilcraft.entity.villager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;

import java.util.Random;

/**
 * Copied from VillagerTrades
 * @author rubensworks
 */
public class EmeraldForItemsTrade implements VillagerTrades.ITrade {
    private final Item tradeItem;
    private final int count;
    private final int maxUses;
    private final int xpValue;
    private final float priceMultiplier;

    public EmeraldForItemsTrade(IItemProvider tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
        this.tradeItem = tradeItemIn.asItem();
        this.count = countIn;
        this.maxUses = maxUsesIn;
        this.xpValue = xpValueIn;
        this.priceMultiplier = 0.05F;
    }

    public MerchantOffer getOffer(Entity trader, Random rand) {
        ItemStack itemstack = new ItemStack(this.tradeItem, this.count);
        return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.xpValue, this.priceMultiplier);
    }
}
