package evilcraft.modcompat;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import evilcraft.modcompat.forestry.ForestryModCompat;
import evilcraft.modcompat.tconstruct.TConstructModCompat;
import evilcraft.modcompat.thermalexpansion.ThermalExpansionModCompat;
import evilcraft.modcompat.waila.WailaModCompat;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader {

    /**
     * The list of mod compatibilities.
     */
    public static List<IModCompat> MODCOMPATS = new LinkedList<IModCompat>();
    static {
        MODCOMPATS.add(new ForestryModCompat());
        MODCOMPATS.add(new ThermalExpansionModCompat());
        MODCOMPATS.add(new TConstructModCompat());
        MODCOMPATS.add(new WailaModCompat());
    }
    
    /**
     * This will be called in the {@link FMLPreInitializationEvent}.
     */
    public static void preInit() {
        for(IModCompat modCompat : MODCOMPATS) {
            if(isModLoaded(modCompat)) {
                modCompat.preInit();
            }
        }
    }
    
    /**
     * This will be called in the {@link FMLInitializationEvent}.
     */
    public static void init() {
        for(IModCompat modCompat : MODCOMPATS) {
            if(isModLoaded(modCompat)) {
                modCompat.init();
            }
        }
    }
    
    /**
     * This will be called in the {@link FMLPostInitializationEvent}.
     */
    public static void postInit() {
        for(IModCompat modCompat : MODCOMPATS) {
            if(isModLoaded(modCompat)) {
                modCompat.postInit();
            }
        }
    }
    
    private static boolean isModLoaded(IModCompat modCompat) {
        return Loader.isModLoaded(modCompat.getModID());
    }
    
}
