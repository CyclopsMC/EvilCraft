package evilcraft.block;

import net.minecraft.item.ItemBlock;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.item.ItemBlockNBT;

/**
 * Config for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class SpiritFurnaceConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static SpiritFurnaceConfig _instance;
    
    /**
     * How much mB per tick this furnace should consume.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "How much mB per tick this furnace should consume.")
    public static int mBPerTick = 5;
    
    /**
     * The required amount of ticks for each HP for cooking an entity.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The required amount of ticks for each HP for cooking an entity.")
    public static int requiredTicksPerHp = 10;

    /**
     * Make a new instance.
     */
    public SpiritFurnaceConfig() {
        super(
        	true,
            "spiritFurnace",
            null,
            SpiritFurnace.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockNBT.class;
    }
    
}
