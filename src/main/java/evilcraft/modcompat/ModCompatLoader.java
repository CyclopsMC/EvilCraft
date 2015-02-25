package evilcraft.modcompat;

import com.google.common.collect.Sets;
import cpw.mods.fml.common.Loader;
import evilcraft.EvilCraft;
import evilcraft.GeneralConfig;
import evilcraft.IInitListener;
import evilcraft.core.config.ConfigHandler;
import evilcraft.modcompat.baubles.BaublesModCompat;
import evilcraft.modcompat.bloodmagic.BloodMagicModCompat;
import evilcraft.modcompat.fmp.ForgeMultipartModCompat;
import evilcraft.modcompat.forestry.ForestryModCompat;
import evilcraft.modcompat.nei.NEIModCompat;
import evilcraft.modcompat.tconstruct.TConstructModCompat;
import evilcraft.modcompat.thaumcraft.ThaumcraftModCompat;
import evilcraft.modcompat.thermalexpansion.ThermalExpansionModCompat;
import evilcraft.modcompat.versionchecker.VersionCheckerModCompat;
import evilcraft.modcompat.waila.WailaModCompat;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        MODCOMPATS.add(new ThaumcraftModCompat());
    }

    protected static Set<String> CRASHED_MODCOMPATS = Sets.newHashSet();
    
    @Override
    public void onInit(IInitListener.Step step) {
        for(IModCompat modCompat : MODCOMPATS) {
            if(shouldLoadModCompat(modCompat)) {
                try {
                    modCompat.onInit(step);
                } catch (RuntimeException e) {
                    EvilCraft.log("The EvilCraft mod compatibility for " + modCompat.getModID() +
                            " has crashed! Report this crash log to the mod author or try updating the conflicting mods.", Level.ERROR);
                    if(GeneralConfig.crashOnModCompatCrash) throw e;
                    e.printStackTrace();
                    CRASHED_MODCOMPATS.add(modCompat.getModID());
                }
            }
        }
    }
    
    /**
     * If the given mod compat should be loaded.
     * @param modCompat The mod compat.
     * @return If it should be loaded.
     */
    public static final boolean shouldLoadModCompat(IModCompat modCompat) {
    	return isModLoaded(modCompat) && isModEnabled(modCompat) && isModNotCrashed(modCompat);
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

    private static boolean isModNotCrashed(IModCompat modCompat) {
        return !CRASHED_MODCOMPATS.contains(modCompat.getModID());
    }
    
}
