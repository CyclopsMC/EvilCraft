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
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.block.BoxOfEternalClosure;
import evilcraft.block.BoxOfEternalClosureConfig;
import evilcraft.block.DarkTank;
import evilcraft.block.DarkTankConfig;
import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.core.recipe.ItemBlockFluidContainerCombinationRecipe;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.recipe.event.IRecipeOutputObserver;
import evilcraft.core.recipe.event.ObservableShapelessRecipe;
import evilcraft.core.recipe.xml.XmlRecipeLoader;
import evilcraft.core.weather.WeatherType;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.item.BloodContainer;
import evilcraft.item.BloodContainerConfig;
import evilcraft.item.InvertedPotentia;
import evilcraft.item.InvertedPotentiaConfig;
import evilcraft.item.PotentiaSphereConfig;
import evilcraft.item.VeinSword;
import evilcraft.item.VeinSwordConfig;
import evilcraft.item.VengeancePickaxe;
import evilcraft.item.VengeancePickaxeConfig;
import evilcraft.item.WeatherContainer;
import evilcraft.item.WeatherContainer.WeatherContainerTypes;
import evilcraft.item.WeatherContainerConfig;

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
    	if(Configs.isEnabled(EnchantmentPoisonTipConfig.class)) {
	    	ItemStack poisonTipEnchant = new ItemStack(Items.enchanted_book);
	        Enchantment enchant = EnchantmentPoisonTip.getInstance();
	        Items.enchanted_book.addEnchantment(poisonTipEnchant, new EnchantmentData(enchant,
	        		enchant.getMinLevel()));
	    	XmlRecipeLoader.registerPredefinedItem("evilcraft:enchanted_book_poisonTip", poisonTipEnchant);
    	}
    
    	if(Configs.isEnabled(WeatherContainerConfig.class)) {
	    	ItemStack lightningWeatherContainer = new ItemStack(WeatherContainer.getInstance(), 1,
	    			WeatherContainerTypes.LIGHTNING.ordinal());
	    	XmlRecipeLoader.registerPredefinedItem("evilcraft:lightningWeatherContainer",
	    			lightningWeatherContainer);
    	}
    	
    	if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
	    	ItemStack invertedPotentiaEmpowered = new ItemStack(InvertedPotentia.getInstance(),
	    			1, InvertedPotentia.EMPOWERED_META);
	    	XmlRecipeLoader.registerPredefinedItem("evilcraft:invertedPotentiaEmpowered",
	    			invertedPotentiaEmpowered);
    	}
    	
    	if(Configs.isEnabled(VengeancePickaxeConfig.class)) {
	    	ItemStack vengeancePickaxeFortune = VengeancePickaxe.createCraftingResult();
	    	XmlRecipeLoader.registerPredefinedItem("evilcraft:vengeancePickaxeFortune",
	    			vengeancePickaxeFortune);
    	}
    	
    	if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
	    	ItemStack boxOfEternalClosureFilled = new ItemStack(BoxOfEternalClosure.getInstance());
	    	BoxOfEternalClosure.setVengeanceSwarmContent(boxOfEternalClosureFilled);
	    	XmlRecipeLoader.registerPredefinedItem("evilcraft:boxOfEternalClosureFilled",
	    			boxOfEternalClosureFilled);
    	}
    	
    	if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
	    	ItemStack empoweredInvertedPotentia = new ItemStack(InvertedPotentia.getInstance());
	        InvertedPotentia.getInstance().empower(empoweredInvertedPotentia);
	        XmlRecipeLoader.registerPredefinedItem("evilcraft:empoweredInvertedPotentia",
	        		empoweredInvertedPotentia);
    	}
    	
    	if(PotentiaSphereConfig.enderPearlRecipe) {
    		XmlRecipeLoader.registerPredefinedValue("evilcraft:enderPearlRecipe");
    	}
    	
    	if(Configs.isEnabled(VeinSwordConfig.class)) {
	    	ItemStack veinSwordLooting = VeinSword.createCraftingResult();
	    	XmlRecipeLoader.registerPredefinedItem("evilcraft:veinSwordLooting",
	    			veinSwordLooting);
    	}
    }
    
    private static void registerRecipesForFile(InputStream is, String fileName) {
    	InputStream xsdIs = Recipes.class.getResourceAsStream(RECIPES_XSD_PATH);
    	XmlRecipeLoader loader = new XmlRecipeLoader(is, fileName);
    	loader.setValidator(xsdIs);
    	loader.loadRecipes();
    }
    
    private static void registerRecipesForFiles(File file) {
    	if(file.isFile() && EXTERNAL_RECIPES_PATTERN.matcher(file.getName()).matches()) {
    		try {
				registerRecipesForFile(new FileInputStream(file), file.getName());
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
	    	registerRecipesForFile(is, file);
    	}
    	
    	// Load all the externally defined recipes.
    	registerRecipesForFiles(rootConfigFolder);

    	// Register remaining recipes that are too complex to declare in xml files.
        registerCustomRecipes();
    }

    private static void registerCustomRecipes() {
    	// Blood Containers to Dark Tank
    	// TODO: remove this conversion and the blood container item in the next EC update.
        if(Configs.isEnabled(BloodContainerConfig.class) && Configs.isEnabled(DarkTankConfig.class)) {
            for(int i = 0; i < BloodContainerConfig.getContainerLevels(); i++) {
            	ItemStack input = new ItemStack(BloodContainer.getInstance(), 1, i);
                if(!BloodContainer.getInstance().isCreativeItem(input)) {
                	final int capacity = BloodContainer.getInstance().getCapacity(input);
                	ItemStack result = new ItemStack(DarkTank.getInstance());
                    GameRegistry.addRecipe(new ObservableShapelessRecipe(result,
                            new Object[]{
                    			input,
                    		},
                            new IRecipeOutputObserver() {

								@Override
								public ItemStack getRecipeOutput(
										InventoryCrafting grid,
										ItemStack output) {
									ItemBlockFluidContainer container = (ItemBlockFluidContainer) output.getItem();
				                	container.setCapacity(output, capacity);
				                	ItemStack input = null;
				                	for(int j = 0; j < grid.getSizeInventory(); j++) {
				            			ItemStack element = grid.getStackInSlot(j);
				            			if(element != null && element.getItem() == BloodContainer.getInstance()) {
				            				input = element;
				            			}
				                	}
				                	if(input == null) {
				                		return null;
				                	}
				                	FluidStack resource = BloodContainer.getInstance().getFluid(input);
				                	if(resource != null && resource.amount > 0) {
				                		container.fill(output, resource, true);
				                	}
									return output;
								}
                    	
                    }
                            ));
                }
            }
        }
        
        // Dark tank upgrades
        if(Configs.isEnabled(DarkTankConfig.class)) {
        	for(int i = 1; i < 9; i++) {
        		ItemBlockFluidContainer tankItem = (ItemBlockFluidContainer) Item.getItemFromBlock(DarkTank.getInstance());
	        	GameRegistry.addRecipe(new ItemBlockFluidContainerCombinationRecipe(i, tankItem));
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
