package evilcraft;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.block.*;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
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
import evilcraft.recipe.BloodExtractorCombinationRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
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
    private static final String EXTERNAL_OVERRIDE_RECIPES = "_override";

    /**
     * Maps tags to lists of recipe output items.
     */
    public static final Multimap<String, ItemStack> taggedOutput = LinkedListMultimap.create();
    /**
     * Maps tags to lists of recipe output configurables.
     */
    public static final Multimap<String, ExtendedConfig<?>> taggedConfigurablesOutput = LinkedListMultimap.create();

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

        if(Configs.isEnabled(DarkTankConfig.class)) {
            ItemStack darkTankx9 = new ItemStack(DarkTank.getInstance());
            ItemBlockFluidContainer item = ((ItemBlockFluidContainer) darkTankx9.getItem());
            item.setCapacity(darkTankx9, item.getCapacity(darkTankx9) * 9);
            XmlRecipeLoader.registerPredefinedItem("evilcraft:darkTankx9", darkTankx9);
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
    
    private static XmlRecipeLoader registerRecipesForFile(InputStream is, String fileName, boolean canOverride) throws XmlRecipeLoader.XmlRecipeException {
    	return new XmlRecipeLoader(is, fileName);
    }
    
    private static List<XmlRecipeLoader> registerRecipesForFiles(File file, Map<String, XmlRecipeLoader> internalLoaders, boolean canOverride) throws XmlRecipeLoader.XmlRecipeException {
    	if(file.isFile() && EXTERNAL_RECIPES_PATTERN.matcher(file.getName()).matches()) {
    		try {
                XmlRecipeLoader loader = registerRecipesForFile(new FileInputStream(file), file.getName(), canOverride);
                if(internalLoaders.containsKey(file.getName()) && canOverride) {
                    // Override the internal recipes file.
                    internalLoaders.put(file.getName(), loader);
                } else {
                    return Lists.newArrayList(loader);
                }
			} catch (FileNotFoundException e) {
				// Very unlikely to happen...
			}
    	} else if(file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if(childFiles != null) {
                List<XmlRecipeLoader> loaders = Lists.newLinkedList();
                for (File childFile : childFiles) {
                    loaders.addAll(registerRecipesForFiles(
                            childFile, internalLoaders, EXTERNAL_OVERRIDE_RECIPES.equals(file.getName())
                    ));
                }
            }
    	}
        return Collections.emptyList();
    }

    private static void loadAllRecipes(Collection<XmlRecipeLoader> loaders) {
        for(XmlRecipeLoader loader : loaders) {
            InputStream xsdIs = Recipes.class.getResourceAsStream(RECIPES_XSD_PATH);
            loader.setValidator(xsdIs);
            loader.loadRecipes(GeneralConfig.crashOnInvalidRecipe);
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
        RecipeSorter.register(Reference.MOD_ID + "bloodextractorcombination", BloodExtractorCombinationRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapedore");

    	loadPredefineds();
    	
    	// Load the recipes stored in XML.
        Map<String, XmlRecipeLoader> internalLoaders = Maps.newHashMapWithExpectedSize(RECIPES_FILES.length);
    	for(String file : RECIPES_FILES) {
	    	InputStream is = Recipes.class.getResourceAsStream(RECIPES_BASE_PATH + file);
            internalLoaders.put(file, registerRecipesForFile(is, file, true));
    	}
    	
    	// Load all the externally defined recipes.
    	List<XmlRecipeLoader> externalLoaders = registerRecipesForFiles(rootConfigFolder, internalLoaders, false);

        loadAllRecipes(internalLoaders.values());
        loadAllRecipes(externalLoaders);

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

        // Blood Extractor upgrades
        if(Configs.isEnabled(BloodExtractorConfig.class) && Configs.isEnabled(DarkTankConfig.class)) {
            for(int i = 1; i < 9; i++) {
                GameRegistry.addRecipe(new BloodExtractorCombinationRecipe(i));
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

            // Add biome extract recipes
            if(Configs.isEnabled(BiomeExtractConfig.class) && BiomeExtractConfig.hasRecipes) {
                ItemStack emptyContainer = new ItemStack(BiomeExtract.getInstance());
                ItemStack filledContainer = BiomeExtract.getInstance().createItemStack(null, 1); // Still dummy!
                filledContainer.setItemDamage(OreDictionary.WILDCARD_VALUE);
                EnvironmentalAccumulator.getInstance().getRecipeRegistry().registerRecipe(
                        recipeName,
                        new EnvironmentalAccumulatorRecipeComponent(
                                emptyContainer,
                                WeatherType.ANY
                        ),
                        new EnvironmentalAccumulatorRecipeComponent(
                                filledContainer,
                                WeatherType.ANY
                        ),
                        new EnvironmentalAccumulatorRecipeProperties(1000, BiomeExtractConfig.envirAccCooldownTime, -1.0D, null, new EnvironmentalAccumulatorRecipeProperties.IEAResultOverride() {
                            @Override
                            public ItemStack getResult(IBlockAccess world, int x, int y, int z, ItemStack originalResult) {
                                World worldSafe = (World) world;
                                BiomeGenBase biome = worldSafe.getBiomeGenForCoords(x, z);
                                if(BiomeExtractConfig._instance.isCraftingBlacklisted(biome.biomeID)) {
                                    return BiomeExtract.getInstance().createItemStack(null, 1);
                                } else {
                                    return BiomeExtract.getInstance().createItemStack(biome, 1);
                                }
                            }
                        })
                );
            }
        }
    }
    
}
