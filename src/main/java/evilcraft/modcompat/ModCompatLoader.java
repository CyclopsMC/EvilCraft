package evilcraft.modcompat;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.Loader;
import evilcraft.IInitListener;
import evilcraft.modcompat.fmp.ForgeMultipartModCompat;
import evilcraft.modcompat.forestry.ForestryModCompat;
import evilcraft.modcompat.tconstruct.TConstructModCompat;
import evilcraft.modcompat.thermalexpansion.ThermalExpansionModCompat;
import evilcraft.modcompat.waila.WailaModCompat;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader implements IInitListener {

    /**
     * The list of mod compatibilities.
     */
    public static List<IModCompat> MODCOMPATS = new LinkedList<IModCompat>();
    static {
        MODCOMPATS.add(new ForestryModCompat());
        MODCOMPATS.add(new ThermalExpansionModCompat());
        MODCOMPATS.add(new TConstructModCompat());
        MODCOMPATS.add(new WailaModCompat());
        MODCOMPATS.add(new ForgeMultipartModCompat());
    }
    
    @Override
    public void onInit(IInitListener.Step step) {
        for(IModCompat modCompat : MODCOMPATS) {
            if(isModLoaded(modCompat)) {
                modCompat.onInit(step);
            }
        }
    }
    
    private static boolean isModLoaded(IModCompat modCompat) {
        return Loader.isModLoaded(modCompat.getModID());
    }
    
}
