package evilcraft.modcompat.forestry;

import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.Reference;
import evilcraft.blocks.UndeadSaplingConfig;
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
        FMLInterModComms.sendMessage(getModID(), "add-farmable-sapling",
                "farmArboreal@" + UndeadSaplingConfig._instance.ID + ".0");
    }

    @Override
    public void postInit() {
        
    }

}
