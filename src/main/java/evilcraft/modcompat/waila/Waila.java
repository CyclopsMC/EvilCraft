package evilcraft.modcompat.waila;

import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.block.*;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.helper.L10NHelpers;
import evilcraft.core.tileentity.TankInventoryTileEntity;
import evilcraft.tileentity.TileBoxOfEternalClosure;
import mcp.mobius.waila.api.IWailaRegistrar;

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
        registrar.addConfig(Reference.MOD_NAME, getTankConfigID(), L10NHelpers.localize("gui.waila.tankConfig"));
        registrar.addConfig(Reference.MOD_NAME, getInnerBlockConfigID(), L10NHelpers.localize("gui.waila.innerBlocksConfig"));
        
        // Tanks
        registrar.registerBodyProvider(new TankDataProvider(), TankInventoryTileEntity.class);
        
        // Inner blocks
        if(Configs.isEnabled(BloodStainedBlockConfig.class))
            registrar.registerStackProvider(new InnerBlockDataProvider(), BloodStainedBlock.class);
        if(Configs.isEnabled(NetherfishSpawnConfig.class))
            registrar.registerStackProvider(new InnerBlockDataProvider(), NetherfishSpawn.class);
        
        // Other blocks
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
        	registrar.registerSyncedNBTKey(TileBoxOfEternalClosure.NBTKEY_SPIRIT, TileBoxOfEternalClosure.class);
            registrar.registerBodyProvider(new BoxOfEternalClosureDataProvider(), TileBoxOfEternalClosure.class);
        }

        // Generic block info
        registrar.registerBodyProvider(new GenericBlockInfoDataProvider(), IConfigurable.class);
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
    public static String getBlockInfoConfigID() {
        return Reference.MOD_NAME + ".genericBlockInfo";
    }
    
}
