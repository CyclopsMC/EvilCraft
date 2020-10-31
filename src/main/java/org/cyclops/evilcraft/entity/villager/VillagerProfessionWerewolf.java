package org.cyclops.evilcraft.entity.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.Reference;

/**
 * Villager with specific evil trades.
 * @author rubensworks
 *
 */
public class VillagerProfessionWerewolf extends VillagerProfession {

    /*private static final Random RANDOM = new Random();
    private static final List<VillagerTrades.ITrade> IN = Lists.newArrayList();
    private static final List<VillagerTrades.ITrade> OUT = Lists.newArrayList();
    static {
        IN.add(new VillagerTrades.EmeraldForItems(ItemDarkGem.getInstance(), new VillagerTrades.PriceInfo(2, 5)));
        IN.add(new VillagerTrades.EmeraldForItems(ItemHardenedBloodShardConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(10, 15)));
        IN.add(new VillagerTrades.EmeraldForItems(ItemBlookConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(5, 9)));
        IN.add(new VillagerTrades.EmeraldForItems(ItemInvertedPotentia.getInstance(), new VillagerTrades.PriceInfo(1, 5)));
        IN.add(new VillagerTrades.EmeraldForItems(ItemPoisonSacConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(10, 15)));

        OUT.add(new VillagerTrades.ListItemForEmeralds(ItemWerewolfBoneConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(5, 10)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(ItemWerewolfFurConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(5, 10)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(ItemBloodInfusionCoreConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(10, 15)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(Item.getItemFromBlock(BlockBoxOfEternalClosure.getInstance()), new VillagerTrades.PriceInfo(1, 2)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(ItemDarkGemCrushedConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(3, 5)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(ItemVengeanceFocus.getInstance(), new VillagerTrades.PriceInfo(12, 18)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(Item.getItemFromBlock(BlockUndeadSaplingConfig._instance.getBlockInstance()), new VillagerTrades.PriceInfo(15, 25)));
        OUT.add(new VillagerTrades.ListItemForEmeralds(ItemGarmonboziaConfig._instance.getItemInstance(), new VillagerTrades.PriceInfo(50, 64)));
    }*/

    public VillagerProfessionWerewolf(VillagerConfig eConfig) {
        super(new ResourceLocation(Reference.MOD_ID, eConfig.getNamedId()).toString(), PointOfInterestType.BUTCHER, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER);

        // TODO: rewrite trades
        /*VillagerTrades.VILLAGER_DEFAULT_TRADES.put(this, new Int2ObjectOpenHashMap<>(ImmutableMap.of(1, new VillagerTrades.ITrade[]{
                new VillagerTrades.EmeraldForItemsTrade(Items.WHEAT, 20, 16, 2),
        })));

        VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(this, eConfig.getNamedId());
        int level = 1;
        level = tryAddTrades(level, career, IN);
        level = tryAddTrades(level, career, OUT);
        level = tryAddTrades(level, career, OUT);
        level = tryAddTrades(level, career, IN);
        level = tryAddTrades(level, career, OUT);*/
    }

    /*protected static int tryAddTrades(int level, VillagerRegistry.VillagerCareer career, List<VillagerTrades.ITrade> trades) {
        Optional<VillagerTrades.ITrade> trade = random(trades);
        if (trade.isPresent()) {
            career.addTrade(level, trade.get());
            return level + 1;
        }
        return level;
    }

    protected static <T> Optional<T> random(List<T> list) {
        return list.size() > 0 ? Optional.of(list.get(RANDOM.nextInt(list.size()))) : Optional.<T>absent();
    }*/
}
