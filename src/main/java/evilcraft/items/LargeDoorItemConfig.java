package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link LargeDoorItem}.
 * @author rubensworks
 *
 */
public class LargeDoorItemConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static LargeDoorItemConfig _instance;

    /**
     * Make a new instance.
     */
    public LargeDoorItemConfig() {
        super(
            "largeDoorItem",
            null,
            LargeDoorItem.class
        );
    }
    
    @Override
    public boolean isForceDisabled() {
        return true;
    }
    
}
