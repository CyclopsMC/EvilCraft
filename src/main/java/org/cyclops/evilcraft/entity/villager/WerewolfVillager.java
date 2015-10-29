package org.cyclops.evilcraft.entity.villager;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableVillager;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.cyclopscore.item.WeightedItemStack;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.block.BoxOfEternalClosureConfig;
import org.cyclops.evilcraft.block.UndeadSaplingConfig;
import org.cyclops.evilcraft.item.*;

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
        
        // Input
        if(Configs.isEnabled(DarkGemConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(DarkGem.getInstance(), 1), 1));
        }
        if(Configs.isEnabled(HardenedBloodShardConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(HardenedBloodShardConfig._instance.getItemInstance(), 1), 2));
        }
        if(Configs.isEnabled(BlookConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(
                    BlookConfig._instance.downCast().getItemInstance(), 1), 5));
        }
        if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(InvertedPotentia.getInstance(), 1, 1), 10));
        }
        if(Configs.isEnabled(PoisonSacConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(PoisonSacConfig._instance.getItemInstance(), 1, 1), 3));
        }
        
        // Output
        if(Configs.isEnabled(WerewolfBoneConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfBoneConfig._instance.getItemInstance(), 1), 10));
        }
        if(Configs.isEnabled(WerewolfFurConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfFurConfig._instance.getItemInstance(), 1), 10));
        }
        if(Configs.isEnabled(BloodInfusionCoreConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(
                    BloodInfusionCoreConfig._instance.downCast().getItemInstance(), 1), 15));
        }
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(BoxOfEternalClosure.getInstance(), 1), 75));
        }
        if(Configs.isEnabled(DarkGemCrushedConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 1), 3));
        }
        if(Configs.isEnabled(VengeanceFocusConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(VengeanceFocus.getInstance(), 1), 30));
        }
        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(UndeadSaplingConfig._instance.getBlockInstance(), 1), 25));
        }
        if(Configs.isEnabled(GarmonboziaConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(GarmonboziaConfig._instance.getItemInstance(), 1), 300));
        }
    }
}
