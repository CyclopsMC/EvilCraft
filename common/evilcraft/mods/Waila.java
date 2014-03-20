package evilcraft.mods;

import mcp.mobius.waila.api.IWailaRegistrar;
import evilcraft.Recipes;
import evilcraft.Reference;
import evilcraft.blocks.BloodChest;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.BloodStainedBlock;
import evilcraft.blocks.BloodStainedBlockConfig;
import evilcraft.blocks.NetherfishSpawn;
import evilcraft.blocks.NetherfishSpawnConfig;
import evilcraft.blocks.Purifier;
import evilcraft.blocks.PurifierConfig;

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
        if(Recipes.isItemEnabled(BloodInfuserConfig.class))
            registrar.registerBodyProvider(new TankDataProvider(), BloodInfuser.class);
        if(Recipes.isItemEnabled(BloodChestConfig.class))
            registrar.registerBodyProvider(new TankDataProvider(), BloodChest.class);
        if(Recipes.isItemEnabled(PurifierConfig.class))
            registrar.registerBodyProvider(new TankDataProvider(), Purifier.class);
        
        // Inner blocks
        if(Recipes.isItemEnabled(BloodStainedBlockConfig.class))
            registrar.registerStackProvider(new InnerBlockDataProvider(), BloodStainedBlock.class);
        if(Recipes.isItemEnabled(NetherfishSpawnConfig.class))
            registrar.registerStackProvider(new InnerBlockDataProvider(), NetherfishSpawn.class);
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
    
}
