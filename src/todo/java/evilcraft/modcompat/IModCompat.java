package evilcraft.modcompat;

import evilcraft.IInitListener;

/**
 * Interface for external mod compatibilities.
 * Implement this on classes that require external mod functionality
 * that needs to be called in the preInit, init or postInit events.
 * Add instances to the {@link ModCompatLoader#MODCOMPATS} list.
 * Note that classes implementing this interface can NOT use classes
 * from the targetted mod, since an instance of the ModCompat will be
 * created anyways, and otherwise certain class definitions won't be found.
 * @author rubensworks
 *
 */
public interface IModCompat extends IInitListener {

    /**
     * Get the unique mod ID.
     * @return The mod ID.
     */
    public String getModID();
    
    /**
     * @return If this mod compat is enabled by default.
     */
    public boolean isEnabled();
    
    /**
     * @return The comment of this mod compat in the config file.
     */
    public String getComment();
    
}
