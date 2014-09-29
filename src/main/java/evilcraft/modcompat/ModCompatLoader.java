package evilcraft.modcompat;

import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Loader;
import evilcraft.IInitListener;
import evilcraft.core.config.ConfigHandler;
import evilcraft.modcompat.baubles.BaublesModCompat;
import evilcraft.modcompat.bloodmagic.BloodMagicModCompat;
import evilcraft.modcompat.fmp.ForgeMultipartModCompat;
import evilcraft.modcompat.forestry.ForestryModCompat;
import evilcraft.modcompat.nei.NEIModCompat;
import evilcraft.modcompat.tconstruct.TConstructModCompat;
import evilcraft.modcompat.thermalexpansion.ThermalExpansionModCompat;
import evilcraft.modcompat.versionchecker.VersionCheckerModCompat;
import evilcraft.modcompat.waila.WailaModCompat;

/**
 * The loader for {@link IModCompat} instances.
 * @author rubensworks
 *
 */
public class ModCompatLoader implements IInitListener {

    /**
     * The list of mod compatibilities that should be called on each init step.
     */
    public static List<IModCompat> MODCOMPATS = new LinkedList<IModCompat>();
    static {
        MODCOMPATS.add(new ForestryModCompat());
        MODCOMPATS.add(new ThermalExpansionModCompat());
        MODCOMPATS.add(new TConstructModCompat());
        MODCOMPATS.add(new WailaModCompat());
        MODCOMPATS.add(new ForgeMultipartModCompat());
        MODCOMPATS.add(new BaublesModCompat());
        MODCOMPATS.add(new NEIModCompat());
        MODCOMPATS.add(new VersionCheckerModCompat());
        MODCOMPATS.add(new BloodMagicModCompat());
    }
    
    @Override
    public void onInit(IInitListener.Step step) {
        for(IModCompat modCompat : MODCOMPATS) {
            if(shouldLoadModCompat(modCompat)) {
                modCompat.onInit(step);
            }
        }
    }
    
    /**
     * If the given mod compat should be loaded.
     * @param modCompat The mod compat.
     * @return If it should be loaded.
     */
    public static final boolean shouldLoadModCompat(IModCompat modCompat) {
    	return isModLoaded(modCompat) && isModEnabled(modCompat);
    }
    
    private static boolean isModLoaded(IModCompat modCompat) {
        return Loader.isModLoaded(modCompat.getModID());
    }
    
    private static boolean isModEnabled(IModCompat modCompat) {
    	Configuration config = ConfigHandler.getInstance().getConfig();
    	Property property = config.get("mod compat", modCompat.getModID(),
    			modCompat.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = modCompat.getComment();
        boolean enabled = property.getBoolean(true);
        if(config.hasChanged()) {
        	config.save();
        }
        return enabled;
    }
    
}
