package evilcraft.entities.villager;

import net.minecraft.item.ItemStack;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.VillagerConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.api.config.configurable.ConfigurableVillager;
import evilcraft.items.BloodInfusionCore;
import evilcraft.items.DarkGem;
import evilcraft.items.HardenedBloodShard;
import evilcraft.items.WerewolfBone;
import evilcraft.items.WerewolfFur;

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
        
        allowedTradeInputs.add(new WeightedItemStack(new ItemStack(DarkGem.getInstance(), 64), 128));
        allowedTradeInputs.add(new WeightedItemStack(new ItemStack(HardenedBloodShard.getInstance(), 64), 320));
        
        allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfBone.getInstance(), 1), 50));
        allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfFur.getInstance(), 1), 100));
        allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(BloodInfusionCore.getInstance(), 1), 100));
    }
}
