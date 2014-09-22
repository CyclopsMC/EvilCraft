package evilcraft.item;

import net.minecraftforge.fluids.FluidContainerRegistry;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link NecromancerStaff}.
 * @author rubensworks
 *
 */
public class NecromancerStaffConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static NecromancerStaffConfig _instance;
    
    /**
     * The capacity of the container.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The capacity of the container.", requiresMcRestart = true)
    public static int capacity = FluidContainerRegistry.BUCKET_VOLUME * 10;
    
    /**
     * The amount of Blood that will be drained per usage.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of Blood that will be drained per usage.", isCommandable = true)
    public static int usage = FluidContainerRegistry.BUCKET_VOLUME * 2;

    /**
     * Make a new instance.
     */
    public NecromancerStaffConfig() {
        super(
            true,
            "necromancerStaff",
            null,
            NecromancerStaff.class
        );
    }
    
}
