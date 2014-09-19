package evilcraft.item;
import evilcraft.client.gui.container.GuiExaltedCrafter;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemGui;
import evilcraft.inventory.container.ContainerExaltedCrafter;

/**
 * A portable crafting table with a built-in ender chest.
 * @author rubensworks
 *
 */
public class ExaltedCrafter extends ItemGui {
    
    private static ExaltedCrafter _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new ExaltedCrafter(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ExaltedCrafter getInstance() {
        return _instance;
    }

    private ExaltedCrafter(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        
        if (MinecraftHelpers.isClientSide())
            setGUI(GuiExaltedCrafter.class);
        
        setContainer(ContainerExaltedCrafter.class);
    }

}
