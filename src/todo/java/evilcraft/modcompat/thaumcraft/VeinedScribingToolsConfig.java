package evilcraft.modcompat.thaumcraft;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.modcompat.thaumcraft.VeinedScribingTools}.
 * @author rubensworks
 *
 */
public class VeinedScribingToolsConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static VeinedScribingToolsConfig _instance;

    /**
     * Make a new instance.
     */
    public VeinedScribingToolsConfig() {
        super(
        	true,
            "veinedScribingTools",
            null,
            VeinedScribingTools.class
        );
    }
    
}
