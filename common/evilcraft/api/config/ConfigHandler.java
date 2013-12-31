package evilcraft.api.config;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig.ConfigProperty;
import evilcraft.api.config.elementtypeaction.BlockAction;
import evilcraft.api.config.elementtypeaction.DummyAction;
import evilcraft.api.config.elementtypeaction.EnchantmentAction;
import evilcraft.api.config.elementtypeaction.EntityAction;
import evilcraft.api.config.elementtypeaction.IElementTypeAction;
import evilcraft.api.config.elementtypeaction.ItemAction;
import evilcraft.api.config.elementtypeaction.LiquidAction;
import evilcraft.api.config.elementtypeaction.MobAction;

/**
 * Create config file and register items & blocks from the given ExtendedConfigs
 * @author Ruben Taelman
 *
 */
public class ConfigHandler extends LinkedHashSet<ExtendedConfig>{
    
    public static final String CATEGORY_ITEM = "item";
    public static final String CATEGORY_BLOCK = "block";
    public static final String CATEGORY_LIQUID = "liquid";
    public static final String CATEGORY_ENTITY = "entity";
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_OREGENERATION = "oregeneration";
    public static final String CATEGORY_ENCHANTMENT = "enchantment";
    public static final String CATEGORY_MOB = "mob";
    
    private static ConfigHandler _instance = null;
    
    private static Map<ElementType, IElementTypeAction> elementTypeActions = new HashMap<ElementType, IElementTypeAction>();
    static {
        elementTypeActions.put(ElementType.BLOCK, new BlockAction());
        elementTypeActions.put(ElementType.BLOCKCONTAINER, new BlockAction());
        elementTypeActions.put(ElementType.ITEM, new ItemAction());
        elementTypeActions.put(ElementType.MOB, new MobAction());
        elementTypeActions.put(ElementType.ENTITY, new EntityAction());
        elementTypeActions.put(ElementType.LIQUID, new LiquidAction());
        elementTypeActions.put(ElementType.ENCHANTMENT, new EnchantmentAction());
        
        elementTypeActions.put(ElementType.DUMMY, new DummyAction());
    }
    
    public static ConfigHandler getInstance() {
        if(_instance == null)
            _instance = new ConfigHandler();
        return _instance;
    }
    
    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements
     * @param event the event from the init methods
     */
    public void handle(FMLPreInitializationEvent event) {
        // You will be able to find the config file in .minecraft/config/ and it will be named EvilCraft.cfg
        // here our Configuration has been instantiated, and saved under the name "config"
        // If the file doesn't already exist, it will be created.
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        // Loading the configuration from its file
        config.load();
        
        for(ExtendedConfig eConfig : this) {
            
            // Save additional properties
            for(ConfigProperty configProperty : eConfig.configProperties) {
                // Sorry, no cleaner solution for this...
                String category = configProperty.getCategory();
                String name = configProperty.getName();
                Object value = configProperty.getValue();
                
                Property additionalProperty = null;
                if(value instanceof Integer) {
                    additionalProperty = config.get(
                        category,
                        name,
                        (int)value
                        );
                    additionalProperty.comment = configProperty.getComment();
                    configProperty.getCallback().run(additionalProperty.getInt());
                } else if(value instanceof Boolean) {
                    additionalProperty = config.get(
                        category,
                        name,
                        (boolean)value
                        );
                    additionalProperty.comment = configProperty.getComment();
                    configProperty.getCallback().run(additionalProperty.getBoolean((boolean)value));
                } else if(value instanceof String) {
                    additionalProperty = config.get(
                        category,
                        name,
                        (String)value
                        );
                    additionalProperty.comment = configProperty.getComment();
                    configProperty.getCallback().run(additionalProperty.getString());
                }
            }
            
            if(eConfig.getHolderType().equals(ElementType.MOB)) { // For entity id's we have to do somethin' special!
                eConfig.ID = EntityRegistry.findGlobalUniqueEntityId();
            }
            
            // Check the type of the element
            ElementType type = eConfig.getHolderType();
            
            // Register the element depending on the type and set the creative tab
            elementTypeActions.get(type).commonRun(eConfig, config);

            if(eConfig.isEnabled()) {
                // Call the listener
                eConfig.onRegistered();

                EvilCraft.log("Registered "+eConfig.NAME);
            }
        }
        
        // Add I18N for the creative tab
        LanguageRegistry.instance().addStringLocalization("itemGroup."+Reference.MOD_NAME, "en_US", Reference.MOD_NAME);
        
        // Saving the configuration to its file
        config.save();
    }
    
}
