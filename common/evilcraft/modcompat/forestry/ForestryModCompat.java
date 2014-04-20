package evilcraft.modcompat.forestry;

import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.Configs;
import evilcraft.Reference;
import evilcraft.blocks.UndeadLogConfig;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.PoisonSacConfig;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for Forestry.
 * @author rubensworks
 *
 */
public class ForestryModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_FORESTRY;
    }

    @Override
    public void preInit() {
        
    }

    @Override
    public void init() {
        // Register the Undead Sapling.
        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-farmable-sapling",
                    "farmArboreal@" + UndeadSaplingConfig._instance.ID + ".0");
        }
        
        // Add dark gem to the miner backpack.
        if(Configs.isEnabled(DarkGemConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
                    "miner@" + DarkGemConfig._instance.ID + ":*");
        }
        
        // Add poison sac to hunter backpack.
        if(Configs.isEnabled(PoisonSacConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
                    "hunter@" + PoisonSacConfig._instance.ID + ":*");
        }
        
        // Add undead log to forester backpack.
        if(Configs.isEnabled(UndeadLogConfig.class)) {
            FMLInterModComms.sendMessage(getModID(), "add-backpack-items",
                    "forester@" + UndeadLogConfig._instance.ID + ":*");
        }
        
        ForestryRecipeManager.register();
    }

    @Override
    public void postInit() {
        
    }

}
