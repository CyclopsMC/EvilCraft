package evilcraft;

import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.block.*;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.core.recipe.ItemBlockFluidContainerCombinationRecipe;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.recipe.event.IRecipeOutputObserver;
import evilcraft.core.recipe.event.ObservableShapedRecipe;
import evilcraft.core.recipe.event.ObservableShapelessRecipe;
import evilcraft.core.recipe.xml.XmlRecipeLoader;
import evilcraft.core.weather.WeatherType;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.item.*;
import evilcraft.item.WeatherContainer.WeatherContainerTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
        "bloodinfuser_convenience.xml",
        "bloodinfuser_mods.xml",
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
    	
    	if(WeatherContainerConfig.shapelessRecipes) {
    		XmlRecipeLoader.registerPredefinedValue("evilcraft:shapelessRecipes");
    	}
    }
    
    private static void registerRecipesForFile(InputStream is, String fileName) throws XmlRecipeLoader.XmlRecipeException {
    	InputStream xsdIs = Recipes.class.getResourceAsStream(RECIPES_XSD_PATH);
    	XmlRecipeLoader loader = new XmlRecipeLoader(is, fileName);
    	loader.setValidator(xsdIs);
    	loader.loadRecipes(GeneralConfig.crashOnInvalidRecipe);
    }
    
    private static void registerRecipesForFiles(File file) throws XmlRecipeLoader.XmlRecipeException {
    	if(file.isFile() && EXTERNAL_RECIPES_PATTERN.matcher(file.getName()).matches()) {
    		try {
				registerRecipesForFile(new FileInputStream(file), file.getName());
			} catch (FileNotFoundException e) {
				
			}
    	} else if(file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if(childFiles != null) {
                for (File childFile : childFiles) {
                    registerRecipesForFiles(childFile);
                }
            }
    	}
    }

    /**
     * Register all the recipes of this mod.
     * @param rootConfigFolder The root config folder for this mod, containing any
     * specific configuration stuff.
     */
    public static void registerRecipes(File rootConfigFolder) throws XmlRecipeLoader.XmlRecipeException {
        // Register custom recipe classes
        RecipeSorter.register(Reference.MOD_ID + "observableshapeless", ObservableShapelessRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register(Reference.MOD_ID + "observableshaped", ObservableShapedRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapedore");
        RecipeSorter.register(Reference.MOD_ID + "containercombination", ItemBlockFluidContainerCombinationRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapedore");

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

        // Entangled Chalice unique id
        if(Configs.isEnabled(EntangledChaliceConfig.class)
                && Configs.isEnabled(DarkGemConfig.class)
                && Configs.isEnabled(CorruptedTearConfig.class)) {
            Item tear = CorruptedTearConfig._instance.getItemInstance();
            GameRegistry.addRecipe(new ObservableShapedRecipe(3, 3, new ItemStack[]{
                    new ItemStack(Items.gold_ingot), new ItemStack(tear), new ItemStack(Items.gold_ingot),
                    new ItemStack(DarkGem.getInstance()), new ItemStack(Items.gold_ingot), new ItemStack(DarkGem.getInstance()),
                    new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot)
            }, new ItemStack(Item.getItemFromBlock(EntangledChalice.getInstance()), 2), new IRecipeOutputObserver() {
                @Override
                public ItemStack getRecipeOutput(InventoryCrafting craftingGrid, ItemStack output) {
                    ItemStack newStack = output.copy();
                    EntangledChaliceItem item = (EntangledChaliceItem) Item.getItemFromBlock(EntangledChalice.getInstance());
                    item.setNextTankID(newStack);
                    return newStack;
                }
            }));

            GameRegistry.addRecipe(new ObservableShapedRecipe(3, 3, new ItemStack[]{
                    new ItemStack(Items.gold_ingot), new ItemStack(tear), new ItemStack(Items.gold_ingot),
                    new ItemStack(DarkGem.getInstance()), new ItemStack(Item.getItemFromBlock(EntangledChalice.getInstance()), 1, OreDictionary.WILDCARD_VALUE), new ItemStack(DarkGem.getInstance()),
                    new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_ingot)
            }, new ItemStack(Item.getItemFromBlock(EntangledChalice.getInstance()), 2), new IRecipeOutputObserver() {
                @Override
                public ItemStack getRecipeOutput(InventoryCrafting craftingGrid, ItemStack output) {
                    ItemStack newStack = output.copy();
                    EntangledChaliceItem item = (EntangledChaliceItem) Item.getItemFromBlock(EntangledChalice.getInstance());
                    String tankID = item.getTankID(craftingGrid.getStackInSlot(4));
                    item.setTankID(newStack, tankID);
                    return newStack;
                }
            }));
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
