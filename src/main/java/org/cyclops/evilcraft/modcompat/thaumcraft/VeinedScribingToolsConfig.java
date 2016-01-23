package org.cyclops.evilcraft.modcompat.thaumcraft;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link org.cyclops.evilcraft.modcompat.thaumcraft.VeinedScribingTools}.
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
            EvilCraft._instance,
        	true,
            "veinedScribingTools",
            null,
            VeinedScribingTools.class
        );
    }
    
}
