package org.cyclops.evilcraft.entity.villager;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.cyclops.cyclopscore.config.configurable.ConfigurableVillager;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.block.BoxOfEternalClosureConfig;
import org.cyclops.evilcraft.block.UndeadSaplingConfig;
import org.cyclops.evilcraft.item.*;

import java.util.List;
import java.util.Random;

/**
 * Villager with specific evil trades.
 * @author rubensworks
 *
 */
public class WerewolfVillager extends ConfigurableVillager {

    private static final Random RANDOM = new Random();
    private static final List<EntityVillager.ITradeList> IN = Lists.newArrayList();
    private static final List<EntityVillager.ITradeList> OUT = Lists.newArrayList();
    static {
        if(Configs.isEnabled(DarkGemConfig.class)) {
            IN.add(new EntityVillager.EmeraldForItems(DarkGem.getInstance(), new EntityVillager.PriceInfo(2, 5)));
        }
        if(Configs.isEnabled(HardenedBloodShardConfig.class)) {
            IN.add(new EntityVillager.EmeraldForItems(HardenedBloodShardConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(10, 15)));
        }
        if(Configs.isEnabled(BlookConfig.class)) {
            IN.add(new EntityVillager.EmeraldForItems(BlookConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(5, 9)));
        }
        if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
            IN.add(new EntityVillager.EmeraldForItems(InvertedPotentia.getInstance(), new EntityVillager.PriceInfo(1, 5)));
        }
        if(Configs.isEnabled(PoisonSacConfig.class)) {
            IN.add(new EntityVillager.EmeraldForItems(PoisonSacConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(10, 15)));
        }

        if(Configs.isEnabled(WerewolfBoneConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(WerewolfBoneConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(5, 10)));
        }
        if(Configs.isEnabled(WerewolfFurConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(WerewolfFurConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(5, 10)));
        }
        if(Configs.isEnabled(BloodInfusionCoreConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(BloodInfusionCoreConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(10, 15)));
        }
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()), new EntityVillager.PriceInfo(1, 2)));
        }
        if(Configs.isEnabled(DarkGemCrushedConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(DarkGemCrushedConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(3, 5)));
        }
        if(Configs.isEnabled(VengeanceFocusConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(VengeanceFocus.getInstance(), new EntityVillager.PriceInfo(12, 18)));
        }
        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(UndeadSaplingConfig._instance.getBlockInstance()), new EntityVillager.PriceInfo(15, 25)));
        }
        if(Configs.isEnabled(GarmonboziaConfig.class)) {
            OUT.add(new EntityVillager.ListItemForEmeralds(GarmonboziaConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(50, 64)));
        }
    }
    
    /**
     * The unique instance.
     */
    private static WerewolfVillager _instance = null;
    
    /**
     * Get the unique instance.
     * @return Unique instance.
     */
    public static WerewolfVillager getInstance() {
        return _instance;
    }

    /**
     * Make a new instance.
     * @param eConfig The config.
     */
    public WerewolfVillager(ExtendedConfig<VillagerConfig> eConfig) {
        super(eConfig);

        VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(this, eConfig.getNamedId());
        int level = 1;
        level = tryAddTrades(level, career, IN);
        level = tryAddTrades(level, career, OUT);
        level = tryAddTrades(level, career, OUT);
        level = tryAddTrades(level, career, IN);
        level = tryAddTrades(level, career, OUT);
    }

    protected static int tryAddTrades(int level, VillagerRegistry.VillagerCareer career, List<EntityVillager.ITradeList> trades) {
        Optional<EntityVillager.ITradeList> trade = random(trades);
        if (trade.isPresent()) {
            career.addTrade(level, trade.get());
            return level + 1;
        }
        return level;
    }

    protected static <T> Optional<T> random(List<T> list) {
        return list.size() > 0 ? Optional.of(list.get(RANDOM.nextInt(list.size()))) : Optional.<T>absent();
    }
}
