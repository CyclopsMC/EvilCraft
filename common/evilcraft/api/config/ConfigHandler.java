package evilcraft.api.config;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import evilcraft.EvilCraft;
import evilcraft.EvilCraftTab;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig.ConfigProperty;

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
    
    private static Map<ElementType, String> CATEGORIES = new HashMap<ElementType, String>();
    static {
        CATEGORIES.put(ElementType.ITEM, ConfigHandler.CATEGORY_ITEM);
        CATEGORIES.put(ElementType.BLOCK, ConfigHandler.CATEGORY_BLOCK);
        CATEGORIES.put(ElementType.MOB, ConfigHandler.CATEGORY_ENTITY);
        CATEGORIES.put(ElementType.ENTITY, ConfigHandler.CATEGORY_ENTITY);
        CATEGORIES.put(ElementType.LIQUID, ConfigHandler.CATEGORY_LIQUID);
    }
    
    private static ConfigHandler _instance = null;
    
    private static Map<ElementType, ElementTypeAction> elementTypeActions = new HashMap<ElementType, ElementTypeAction>();
    static {
        elementTypeActions.put(ElementType.BLOCK, new ElementTypeAction(){
            @Override
            public void run(ExtendedConfig eConfig, Configuration config) {
                // Get property in config file and set comment
                Property property = config.getBlock(CATEGORIES.get(eConfig.getHolderType()), eConfig.NAME, eConfig.ID);
                property.comment = eConfig.COMMENT;
                
                // Update the ID, it could've changed
                eConfig.ID = property.getInt();
                
                // Save the config inside the correct element
                eConfig.save();
                
                Block block = (Block) eConfig.getSubInstance();
                
                // Register
                GameRegistry.registerBlock(
                        block,
                        eConfig.getSubUniqueName()
                );
                
                // Set creative tab
                block.setCreativeTab(EvilCraftTab.getInstance());
                
                // Add I18N
                LanguageRegistry.addName(eConfig.getSubInstance(), eConfig.NAME);
                }
        });
        elementTypeActions.put(ElementType.ITEM, new ElementTypeAction(){
            @Override
            public void run(ExtendedConfig eConfig, Configuration config) {
                // Get property in config file and set comment
                Property property = config.getItem(CATEGORIES.get(eConfig.getHolderType()), eConfig.NAME, eConfig.ID);
                property.comment = eConfig.COMMENT;
                
                // Update the ID, it could've changed
                eConfig.ID = property.getInt();
                
                // Save the config inside the correct element
                eConfig.save();
                
                Item item = (Item) eConfig.getSubInstance();
                
                // Register
                GameRegistry.registerItem(
                        item,
                        eConfig.getSubUniqueName()
                );
                
                // Set creative tab
                item.setCreativeTab(EvilCraftTab.getInstance());
                
                // Add I18N
                LanguageRegistry.addName(eConfig.getSubInstance(), eConfig.NAME);
            }
        });
        elementTypeActions.put(ElementType.MOB, new ElementTypeAction(){
            @Override
            public void run(ExtendedConfig eConfig, Configuration config) {
                // Save the config inside the correct element
                eConfig.save();
                
                // Register
                EntityRegistry.registerGlobalEntityID(
                        eConfig.ELEMENT,
                        eConfig.getSubUniqueName(),
                        eConfig.ID, 3515848, 12102 // TODO: tmp egg
                );
                
                // Add I18N
                LanguageRegistry.instance().addStringLocalization("entity."+eConfig.NAMEDID+".name", eConfig.NAME);
            }
        });
        elementTypeActions.put(ElementType.LIQUID, new ElementTypeAction(){
            @Override
            public void run(ExtendedConfig eConfig, Configuration config) {
                // Save the config inside the correct element
                eConfig.save();
                
                // Register
                FluidRegistry.registerFluid((Fluid) eConfig.getSubInstance());
                
                // Add I18N
                //LanguageRegistry.instance().addStringLocalization("fluid."+eConfig.NAMEDID+".name", eConfig.NAME);
            }
        });
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
                Property additionalProperty = config.get(
                            configProperty.getCategory(),
                            configProperty.getName(),
                            configProperty.getValue()
                        );
                additionalProperty.comment = configProperty.getComment();
                configProperty.getCallback().run(additionalProperty.getInt());
            }
            
            if(eConfig.getHolderType().equals(ElementType.MOB)) { // For entity id's we have to do somethin' special!
                eConfig.ID = EntityRegistry.findGlobalUniqueEntityId();
            }
            
            // Check the type of the element
            ElementType type = eConfig.getHolderType();
            
            // Register the element depending on the type and set the creative tab
            elementTypeActions.get(type).commonRun(eConfig, config);
            
            // Call the listener
            eConfig.onRegistered();
            
            EvilCraft.log("Registered "+eConfig.NAME);
        }
        
        // Add I18N for the creative tab
        LanguageRegistry.instance().addStringLocalization("itemGroup."+Reference.MOD_NAME, "en_US", Reference.MOD_NAME);
        
        // Saving the configuration to its file
        config.save();
    }
    
}
