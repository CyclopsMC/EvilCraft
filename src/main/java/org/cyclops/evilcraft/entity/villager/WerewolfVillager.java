package org.cyclops.evilcraft.entity.villager;

import com.google.common.collect.Lists;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.configurable.ConfigurableVillager;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.block.BoxOfEternalClosureConfig;
import org.cyclops.evilcraft.block.UndeadSaplingConfig;
import org.cyclops.evilcraft.item.*;

import java.util.List;

/**
 * Villager with specific evil trades.
 * @author rubensworks
 *
 */
public class WerewolfVillager extends ConfigurableVillager {
    
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

        List<List<EntityVillager.ITradeList>> trades = Lists.newArrayList();

        // Input
        List<EntityVillager.ITradeList> phase1 = Lists.newArrayList();
        if(Configs.isEnabled(DarkGemConfig.class)) {
            phase1.add(new EntityVillager.EmeraldForItems(DarkGem.getInstance(), new EntityVillager.PriceInfo(2, 5)));
        }
        if(Configs.isEnabled(HardenedBloodShardConfig.class)) {
            phase1.add(new EntityVillager.EmeraldForItems(HardenedBloodShardConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(10, 15)));
        }
        if(Configs.isEnabled(BlookConfig.class)) {
            phase1.add(new EntityVillager.EmeraldForItems(BlookConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(5, 9)));
        }
        if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
            phase1.add(new EntityVillager.EmeraldForItems(InvertedPotentia.getInstance(), new EntityVillager.PriceInfo(1, 5)));
        }
        if(Configs.isEnabled(PoisonSacConfig.class)) {
            phase1.add(new EntityVillager.EmeraldForItems(PoisonSacConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(10, 15)));
        }
        trades.add(phase1);
        
        // Output
        List<EntityVillager.ITradeList> phase2 = Lists.newArrayList();
        if(Configs.isEnabled(WerewolfBoneConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(WerewolfBoneConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(5, 10)));
        }
        if(Configs.isEnabled(WerewolfFurConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(WerewolfFurConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(5, 10)));
        }
        if(Configs.isEnabled(BloodInfusionCoreConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(BloodInfusionCoreConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(10, 15)));
        }
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()), new EntityVillager.PriceInfo(1, 2)));
        }
        if(Configs.isEnabled(DarkGemCrushedConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(DarkGemCrushedConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(3, 5)));
        }
        if(Configs.isEnabled(VengeanceFocusConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(VengeanceFocus.getInstance(), new EntityVillager.PriceInfo(12, 18)));
        }
        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(UndeadSaplingConfig._instance.getBlockInstance()), new EntityVillager.PriceInfo(15, 25)));
        }
        if(Configs.isEnabled(GarmonboziaConfig.class)) {
            phase2.add(new EntityVillager.ListItemForEmeralds(GarmonboziaConfig._instance.getItemInstance(), new EntityVillager.PriceInfo(50, 64)));
        }

        // TODO: enable when Forge makes this public
        //(new VillagerRegistry.VillagerCareer(this, eConfig.getNamedId())).init(tradeListToArray(trades));
    }

    // TODO: abstract
    protected static EntityVillager.ITradeList[][] tradeListToArray(List<List<EntityVillager.ITradeList>> trades) {
        EntityVillager.ITradeList[][] result = new EntityVillager.ITradeList[trades.size()][];
        int i = 0;
        for (List<EntityVillager.ITradeList> tradeList : trades) {
            EntityVillager.ITradeList[] tradeArray = tradeList.toArray(new EntityVillager.ITradeList[tradeList.size()]);
            result[i++] = tradeArray;
        }
        return result;
    }
}
