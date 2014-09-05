package evilcraft.entities.villager;

import net.minecraft.item.ItemStack;
import evilcraft.Configs;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.BoxOfEternalClosureConfig;
import evilcraft.blocks.UndeadSapling;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.core.config.ElementType;
import evilcraft.core.config.ExtendedConfig;
import evilcraft.core.config.VillagerConfig;
import evilcraft.core.config.configurable.Configurable;
import evilcraft.core.config.configurable.ConfigurableVillager;
import evilcraft.items.BloodInfusionCore;
import evilcraft.items.BloodInfusionCoreConfig;
import evilcraft.items.Blook;
import evilcraft.items.BlookConfig;
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkGemCrushed;
import evilcraft.items.DarkGemCrushedConfig;
import evilcraft.items.HardenedBloodShard;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.InvertedPotentia;
import evilcraft.items.InvertedPotentiaConfig;
import evilcraft.items.PoisonSac;
import evilcraft.items.PoisonSacConfig;
import evilcraft.items.VengeanceFocus;
import evilcraft.items.VengeanceFocusConfig;
import evilcraft.items.WerewolfBone;
import evilcraft.items.WerewolfBoneConfig;
import evilcraft.items.WerewolfFur;
import evilcraft.items.WerewolfFurConfig;

/**
 * Villager with specific evil trades.
 * @author rubensworks
 *
 */
public class WerewolfVillager extends ConfigurableVillager{
    
    /**
     * The type for this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.VILLAGER;
    
    /**
     * The unique instance.
     */
    private static WerewolfVillager _instance = null;
    
    /**
     * Initialize the unique instance.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<VillagerConfig> eConfig) {
        if(_instance == null)
            _instance = new WerewolfVillager(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
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
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(HardenedBloodShard.getInstance(), 1), 2));
        }
        if(Configs.isEnabled(BlookConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(Blook.getInstance(), 1), 5));
        }
        if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(InvertedPotentia.getInstance(), 1, 1), 10));
        }
        if(Configs.isEnabled(PoisonSacConfig.class)) {
            allowedTradeInputs.add(new WeightedItemStack(new ItemStack(PoisonSac.getInstance(), 1, 1), 3));
        }
        
        // Output
        if(Configs.isEnabled(WerewolfBoneConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfBone.getInstance(), 1), 10));
        }
        if(Configs.isEnabled(WerewolfFurConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfFur.getInstance(), 1), 10));
        }
        if(Configs.isEnabled(BloodInfusionCoreConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(BloodInfusionCore.getInstance(), 1), 15));
        }
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(BoxOfEternalClosure.getInstance(), 1), 75));
        }
        if(Configs.isEnabled(DarkGemCrushedConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(DarkGemCrushed.getInstance(), 1), 3));
        }
        if(Configs.isEnabled(VengeanceFocusConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(VengeanceFocus.getInstance(), 1), 30));
        }
        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(UndeadSapling.getInstance(), 1), 25));
        }
    }
}
