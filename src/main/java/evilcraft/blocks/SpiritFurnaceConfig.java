package evilcraft.blocks;

import net.minecraft.item.ItemBlock;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.api.item.ItemBlockNBT;

/**
 * Config for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class SpiritFurnaceConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static SpiritFurnaceConfig _instance;
    
    /**
     * How much mB per tick this furnace should consume.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "How much mB per tick this furnace should consume.")
    public static int mBPerTick = 5;
    
    /**
     * The required amount of ticks for each HP for cooking an entity.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The required amount of ticks for each HP for cooking an entity.")
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
