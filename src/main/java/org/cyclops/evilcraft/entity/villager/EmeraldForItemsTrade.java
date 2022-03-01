package org.cyclops.evilcraft.entity.villager;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import java.util.Random;

/**
 * Copied from VillagerTrades
 * @author rubensworks
 */
public class EmeraldForItemsTrade implements VillagerTrades.ItemListing {
    private final Item tradeItem;
    private final int count;
    private final int maxUses;
    private final int xpValue;
    private final float priceMultiplier;

    public EmeraldForItemsTrade(ItemLike tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
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
