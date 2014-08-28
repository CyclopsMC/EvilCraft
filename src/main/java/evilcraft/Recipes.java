package evilcraft;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.recipes.CustomRecipe;
import evilcraft.api.recipes.CustomRecipeRegistry;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipe;
import evilcraft.api.recipes.EnvironmentalAccumulatorResult;
import evilcraft.api.weather.WeatherType;
import evilcraft.api.xml.XmlRecipeLoader;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.DarkBloodBrick;
import evilcraft.blocks.DarkBloodBrickConfig;
import evilcraft.blocks.DarkBrick;
import evilcraft.blocks.DarkBrickConfig;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.blocks.UndeadSapling;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.fluids.Blood;
import evilcraft.fluids.BloodConfig;
import evilcraft.items.BloodContainer;
import evilcraft.items.BloodContainerConfig;
import evilcraft.items.Blook;
import evilcraft.items.BlookConfig;
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkPowerGem;
import evilcraft.items.DarkPowerGemConfig;
import evilcraft.items.InvertedPotentia;
import evilcraft.items.InvertedPotentiaConfig;
import evilcraft.items.PotentiaSphere;
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
	
	private static final String RECIPES_PATH = "/assets/" + Reference.MOD_ID + "/recipes.xml";
	private static final String RECIPES_XSD_PATH = "/assets/" + Reference.MOD_ID + "/recipes.xsd";

    /**
     * The extra buckets that are added with this mod.
     */
    public static Map<Item, FluidStack> BUCKETS = new HashMap<Item, FluidStack>();
    
    private static void loadPredefinedItems() {
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
    }

    /**
     * Register all the recipes of this mod.
     */
    public static void registerRecipes() {
    	loadPredefinedItems();
    	
    	// Load the recipes stored in XML.
    	InputStream is = Recipes.class.getResourceAsStream(RECIPES_PATH);
    	InputStream xsdIs = Recipes.class.getResourceAsStream(RECIPES_XSD_PATH);
    	XmlRecipeLoader loader = new XmlRecipeLoader(is);
    	loader.setValidator(xsdIs);
    	loader.loadRecipes();
    	
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

        registerCustomRecipes();
    }

    private static void registerCustomRecipes() {
        if(Configs.isEnabled(BloodInfuserConfig.class)) {
        	// Dark power gem
        	if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkPowerGemConfig.class) && Configs.isEnabled(BloodConfig.class)) {
                CustomRecipeRegistry.put(new CustomRecipe(
                        new ItemStack(DarkGem.getInstance()),
                        new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME / 4),
                        BloodInfuser.getInstance(),
                        200
                        ),
                        new ItemStack(DarkPowerGem.getInstance()
                                ));
            }
        	// Undead sapling
            if(Configs.isEnabled(UndeadSaplingConfig.class)) {
                CustomRecipeRegistry.put(new CustomRecipe(
                        new ItemStack(Blocks.deadbush),
                        new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME * 2),
                        BloodInfuser.getInstance(),
                        200
                        ),
                        new ItemStack(UndeadSapling.getInstance()
                                ));
            }
            // Blook
            if(Configs.isEnabled(BlookConfig.class)) {
                CustomRecipeRegistry.put(new CustomRecipe(
                        new ItemStack(Items.book),
                        new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME / 2),
                        BloodInfuser.getInstance(),
                        500
                        ),
                        new ItemStack(Blook.getInstance()
                                ));
            }
            // Ender pearl
            if(Configs.isEnabled(PotentiaSphereConfig.class) && PotentiaSphereConfig.enderPearlRecipe) {
                CustomRecipeRegistry.put(new CustomRecipe(
                        new ItemStack(PotentiaSphere.getInstance()),
                        new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME * 2),
                        BloodInfuser.getInstance(),
                        1000
                        ),
                        new ItemStack(Items.ender_pearl
                                ));
            }
            // Dark blood brick
            if(Configs.isEnabled(DarkBrickConfig.class) && Configs.isEnabled(DarkBloodBrickConfig.class)) {
                CustomRecipeRegistry.put(new CustomRecipe(
                        new ItemStack(DarkBrick.getInstance()),
                        new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME / 2),
                        BloodInfuser.getInstance(),
                        250
                        ),
                        new ItemStack(DarkBloodBrick.getInstance()
                                ));
            }
        }
        
        if (Configs.isEnabled(EnvironmentalAccumulatorConfig.class)) {
            EnvironmentalAccumulatorRecipe recipe = null;
            EnvironmentalAccumulatorResult result = null;
            
            // Add the different weather container recipes
            if (Configs.isEnabled(WeatherContainerConfig.class)) {
	            ItemStack emptyContainer = WeatherContainer.createItemStack(WeatherContainerTypes.EMPTY, 1);
	            WeatherType[] inputs = {WeatherType.CLEAR, WeatherType.LIGHTNING, WeatherType.RAIN};
	            WeatherType[] outputs = {WeatherType.RAIN, WeatherType.RAIN, WeatherType.CLEAR};
	            
	            for (int i=0; i < inputs.length; ++i) {
	                recipe = new EnvironmentalAccumulatorRecipe(
	                        "WeatherContainer" + inputs[i].getClass().getSimpleName(),
	                        emptyContainer,
	                        inputs[i]
	                );
	                
	                result = new EnvironmentalAccumulatorResult(
	                        recipe,
	                        WeatherContainer.createItemStack(
	                                WeatherContainerTypes.getWeatherContainerType(inputs[i]), 1
	                        ),
	                        outputs[i]
	                );
	                CustomRecipeRegistry.put(recipe, result);
	            }
            }
            
            // Add Empowered Inverted Potentia recipe.
            if(Configs.isEnabled(InvertedPotentiaConfig.class)) {
                recipe = new EnvironmentalAccumulatorRecipe(
                        "EAInvertedPotentia",
                        new ItemStack(InvertedPotentia.getInstance()),
                        WeatherType.LIGHTNING
                );
                
                ItemStack out = new ItemStack(InvertedPotentia.getInstance());
                InvertedPotentia.empower(out);
                result = new EnvironmentalAccumulatorResult(
                        recipe,
                        out,
                        WeatherType.RAIN
                );
                CustomRecipeRegistry.put(recipe, result);
            }
        }
    }
    
}
