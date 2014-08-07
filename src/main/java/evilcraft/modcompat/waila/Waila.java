package evilcraft.modcompat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.blocks.BloodChest;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.BoxOfEternalClosureConfig;
import evilcraft.blocks.NetherfishSpawn;
import evilcraft.blocks.NetherfishSpawnConfig;
import evilcraft.blocks.Purifier;
import evilcraft.blocks.PurifierConfig;
import evilcraft.entities.monster.VengeanceSpirit;
import evilcraft.entities.monster.VengeanceSpiritConfig;

/**
 * Waila support class.
 * @author rubensworks
 *
 */
public class Waila {
    
    /**
     * Waila callback.
     * @param registrar The Waila registrar.
     */
    public static void callbackRegister(IWailaRegistrar registrar){
        registrar.addConfig(Reference.MOD_NAME, getTankConfigID(), "Tank contents");
        registrar.addConfig(Reference.MOD_NAME, getInnerBlockConfigID(), "Actual inner blocks");
        
        // Tanks
        if(Configs.isEnabled(BloodInfuserConfig.class))
            registrar.registerBodyProvider(new TankDataProvider(), BloodInfuser.class);
        if(Configs.isEnabled(BloodChestConfig.class))
            registrar.registerBodyProvider(new TankDataProvider(), BloodChest.class);
        if(Configs.isEnabled(PurifierConfig.class))
            registrar.registerBodyProvider(new TankDataProvider(), Purifier.class);
        
        // Inner blocks
        if(Configs.isEnabled(BloodStainedBlockConfig.class))
            registrar.registerStackProvider(new InnerBlockDataProvider(), BloodStainedBlock.class);
        if(Configs.isEnabled(NetherfishSpawnConfig.class))
            registrar.registerStackProvider(new InnerBlockDataProvider(), NetherfishSpawn.class);
        
        // Other blocks
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class))
            registrar.registerBodyProvider(new BoxOfEternalClosureDataProvider(), BoxOfEternalClosure.class);
        
        // Entities
        if(Configs.isEnabled(VengeanceSpiritConfig.class))
        	registrar.registerOverrideEntityProvider(new VengeanceSpiritDataProvider(), VengeanceSpirit.class);
    }
    
    /**
     * Config ID.
     * @return The config ID.
     */
    public static String getTankConfigID() {
        return Reference.MOD_NAME + ".tank";
    }
    
    /**
     * Config ID.
     * @return The config ID.
     */
    public static String getInnerBlockConfigID() {
        return Reference.MOD_NAME + ".innerBlock";
    }
    
    /**
     * Config ID.
     * @return The config ID.
     */
    public static String getBoxOfEternalClosureConfigID() {
        return Reference.MOD_NAME + ".boxOfEternalClosure";
    }
    
    /**
     * Config ID.
     * @return The config ID.
     */
    public static String getVengeanceSpiritConfigID() {
        return Reference.MOD_NAME + ".vengeanceSpirit";
    }
    
}
