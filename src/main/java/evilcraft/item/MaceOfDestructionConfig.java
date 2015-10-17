package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link MaceOfDestruction}.
 * @author rubensworks
 *
 */
public class MaceOfDestructionConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static MaceOfDestructionConfig _instance;

    /**
     * Make a new instance.
     */
    public MaceOfDestructionConfig() {
        super(
            true,
            "maceOfDestruction",
            null,
            MaceOfDestruction.class
        );
    }
    
}
