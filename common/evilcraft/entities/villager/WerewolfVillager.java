package evilcraft.entities.villager;

import net.minecraft.item.ItemStack;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.VillagerConfig;
import evilcraft.api.config.configurable.ConfigurableVillager;
import evilcraft.items.BloodInfusionCore;
import evilcraft.items.DarkGem;
import evilcraft.items.HardenedBloodShard;
import evilcraft.items.WerewolfBone;
import evilcraft.items.WerewolfFur;

public class WerewolfVillager extends ConfigurableVillager{
    
    public static ElementType TYPE = ElementType.VILLAGER;
    
    private static WerewolfVillager _instance = null;
    
    public static void initInstance(ExtendedConfig<VillagerConfig> eConfig) {
        if(_instance == null)
            _instance = new WerewolfVillager(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    public static WerewolfVillager getInstance() {
        return _instance;
    }

    public WerewolfVillager(ExtendedConfig eConfig) {
        super(eConfig);
        
        allowedTradeInputs.add(new WeightedItemStack(new ItemStack(DarkGem.getInstance(), 64), 128));
        allowedTradeInputs.add(new WeightedItemStack(new ItemStack(HardenedBloodShard.getInstance(), 64), 320));
        
        allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfBone.getInstance(), 1), 50));
        allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(WerewolfFur.getInstance(), 1), 100));
        allowedTradeOutputs.add(new WeightedItemStack(new ItemStack(BloodInfusionCore.getInstance(), 1), 100));
    }
}
