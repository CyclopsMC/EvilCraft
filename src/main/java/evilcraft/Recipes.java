package evilcraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.api.recipes.xml.XmlRecipeLoader;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.items.BloodContainer;
import evilcraft.items.BloodContainerConfig;
import evilcraft.items.InvertedPotentia;
import evilcraft.items.PotentiaSphereConfig;
import evilcraft.items.VengeancePickaxe;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;
import evilcraft.items.WeatherContainerConfig;

/**
 * Holder class of all the recipes.
 * @author rubensworks
 *
 */
public class Recipes {
	
	private static final String[] RECIPES_FILES = {
		"shaped.xml",
		"shapeless.xml",
		"smelting.xml",
		"bloodinfuser.xml",
		"environmentalaccumulator.xml"
	};
	private static final String RECIPES_BASE_PATH = "/assets/" + Reference.MOD_ID + "/recipes/";
	private static final String RECIPES_XSD_PATH = RECIPES_BASE_PATH + "recipes.xsd";
	private static final Pattern EXTERNAL_RECIPES_PATTERN = Pattern.compile("^[^_].*\\.xml");

    /**
     * The extra buckets that are added with this mod.
     */
    public static Map<Item, FluidStack> BUCKETS = new HashMap<Item, FluidStack>();
    
    private static void loadPredefineds() {
    	ItemStack poisonTipEnchant = new ItemStack(Items.enchanted_book);
        Enchantment enchant = EnchantmentPoisonTip.getInstance();
        Items.enchanted_book.addEnchantment(poisonTipEnchant, new EnchantmentData(enchant,
        		enchant.getMinLevel()));
    	XmlRecipeLoader.registerPredefinedItem("evilcraft:enchanted_book_poisonTip", poisonTipEnchant);
    
    	ItemStack lightningWeatherContainer = new ItemStack(WeatherContainer.getInstance(), 1,
    			WeatherContainerTypes.LIGHTNING.ordinal());
    	XmlRecipeLoader.registerPredefinedItem("evilcraft:lightningWeatherContainer",
    			lightningWeatherContainer);
    	
    	ItemStack invertedPotentiaEmpowered = new ItemStack(InvertedPotentia.getInstance(),
    			1, InvertedPotentia.EMPOWERED_META);
    	XmlRecipeLoader.registerPredefinedItem("evilcraft:invertedPotentiaEmpowered",
    			invertedPotentiaEmpowered);
    	
    	ItemStack vengeancePickaxeFortune = VengeancePickaxe.createCraftingResult();
    	XmlRecipeLoader.registerPredefinedItem("evilcraft:vengeancePickaxeFortune",
    			vengeancePickaxeFortune);
    	
    	ItemStack boxOfEternalClosureFilled = new ItemStack(BoxOfEternalClosure.getInstance());
    	BoxOfEternalClosure.setVengeanceSwarmContent(boxOfEternalClosureFilled);
    	XmlRecipeLoader.registerPredefinedItem("evilcraft:boxOfEternalClosureFilled",
    			vengeancePickaxeFortune);
    	
    	ItemStack empoweredInvertedPotentia = new ItemStack(InvertedPotentia.getInstance());
        InvertedPotentia.empower(empoweredInvertedPotentia);
        XmlRecipeLoader.registerPredefinedItem("evilcraft:empoweredInvertedPotentia",
        		empoweredInvertedPotentia);
    	
    	if(PotentiaSphereConfig.enderPearlRecipe) {
    		XmlRecipeLoader.registerPredefinedValue("evilcraft:enderPearlRecipe");
    	}
    }
    
    private static void registerRecipesForFile(InputStream is) {
    	InputStream xsdIs = Recipes.class.getResourceAsStream(RECIPES_XSD_PATH);
    	XmlRecipeLoader loader = new XmlRecipeLoader(is);
    	loader.setValidator(xsdIs);
    	loader.loadRecipes();
    }
    
    private static void registerRecipesForFiles(File file) {
    	if(file.isFile() && EXTERNAL_RECIPES_PATTERN.matcher(file.getName()).matches()) {
    		try {
				registerRecipesForFile(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				
			}
    	} else if(file.isDirectory()) {
    		for(File childFile : file.listFiles()) {
    			registerRecipesForFiles(childFile);
    		}
    	}
    }

    /**
     * Register all the recipes of this mod.
     * @param rootConfigFolder The root config folder for this mod, containing any
     * specific configuration stuff.
     */
    public static void registerRecipes(File rootConfigFolder) {
    	loadPredefineds();
    	
    	// Load the recipes stored in XML.
    	for(String file : RECIPES_FILES) {
	    	InputStream is = Recipes.class.getResourceAsStream(RECIPES_BASE_PATH + file);
	    	registerRecipesForFile(is);
    	}
    	
    	// Load all the externally defined recipes.
    	registerRecipesForFiles(rootConfigFolder);

    	// Register remaining recipes that are too complex to declare in xml files.
        registerCustomRecipes();
    }

    private static void registerCustomRecipes() {
    	// Blood Containers
        if(Configs.isEnabled(BloodContainerConfig.class)) {
            for(int i = 1; i < BloodContainerConfig.getContainerLevels(); i++) {
                ItemStack result = new ItemStack(BloodContainer.getInstance(), 1, i);
                if(!BloodContainer.getInstance().isCreativeItem(result)) {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(result,
                            new ItemStack(BloodContainer.getInstance(), 1, i - 1),
                            new ItemStack(BloodContainer.getInstance(), 1, i - 1)
                            ));
                }
            }
        }
    	
        if (Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            ItemStack outputStack = null;
            String recipeName = null;
            
            // Add the different weather container recipes
            if (Configs.isEnabled(WeatherContainerConfig.class)) {
	            ItemStack emptyContainer = WeatherContainer.createItemStack(WeatherContainerTypes.EMPTY, 1);
	            WeatherType[] weatherInputs = {WeatherType.CLEAR, WeatherType.LIGHTNING, WeatherType.RAIN};
	            WeatherType[] weatherOutputs = {WeatherType.RAIN, WeatherType.RAIN, WeatherType.CLEAR};
	            
	            for (int i=0; i < weatherInputs.length; ++i) {
                    recipeName = "WeatherContainer" + weatherInputs[i].getClass().getSimpleName();
                    outputStack = WeatherContainer.createItemStack(
                            WeatherContainerTypes.getWeatherContainerType(weatherInputs[i]), 1);

                    EnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                            recipeName,
                            new EnvironmentalAccumulatorRecipeComponent(
                                    emptyContainer,
                                    weatherInputs[i]
                            ),
                            new EnvironmentalAccumulatorRecipeComponent(
                                    outputStack,
                                    weatherOutputs[i]
                            ),
                            new EnvironmentalAccumulatorRecipeProperties()
                    );
	            }
            }
        }
    }
    
}
