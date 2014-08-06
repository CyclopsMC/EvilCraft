package evilcraft;

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
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.api.recipes.CustomRecipe;
import evilcraft.api.recipes.CustomRecipeRegistry;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipe;
import evilcraft.api.recipes.EnvironmentalAccumulatorResult;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.BloodChest;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.BoxOfEternalClosureConfig;
import evilcraft.blocks.DarkBlock;
import evilcraft.blocks.DarkBlockConfig;
import evilcraft.blocks.DarkBloodBrick;
import evilcraft.blocks.DarkBloodBrickConfig;
import evilcraft.blocks.DarkBrick;
import evilcraft.blocks.DarkBrickConfig;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.blocks.LightningBomb;
import evilcraft.blocks.LightningBombConfig;
import evilcraft.blocks.ObscuredGlass;
import evilcraft.blocks.ObscuredGlassConfig;
import evilcraft.blocks.Purifier;
import evilcraft.blocks.PurifierConfig;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.blocks.SpiritFurnaceConfig;
import evilcraft.blocks.UndeadLog;
import evilcraft.blocks.UndeadLogConfig;
import evilcraft.blocks.UndeadPlank;
import evilcraft.blocks.UndeadPlankConfig;
import evilcraft.blocks.UndeadSapling;
import evilcraft.blocks.UndeadSaplingConfig;
import evilcraft.enchantment.EnchantmentPoisonTip;
import evilcraft.enchantment.EnchantmentPoisonTipConfig;
import evilcraft.fluids.Blood;
import evilcraft.fluids.BloodConfig;
import evilcraft.items.BloodContainer;
import evilcraft.items.BloodContainerConfig;
import evilcraft.items.BloodExtractor;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.items.BloodInfusionCore;
import evilcraft.items.BloodInfusionCoreConfig;
import evilcraft.items.BloodPearlOfTeleportation;
import evilcraft.items.BloodPearlOfTeleportationConfig;
import evilcraft.items.Blook;
import evilcraft.items.BlookConfig;
import evilcraft.items.BucketPoison;
import evilcraft.items.BucketPoisonConfig;
import evilcraft.items.BurningGemStone;
import evilcraft.items.BurningGemStoneConfig;
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkGemCrushed;
import evilcraft.items.DarkGemCrushedConfig;
import evilcraft.items.DarkPowerGem;
import evilcraft.items.DarkPowerGemConfig;
import evilcraft.items.DarkStick;
import evilcraft.items.DarkStickConfig;
import evilcraft.items.HardenedBloodShard;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.InvertedPotentia;
import evilcraft.items.InvertedPotentiaConfig;
import evilcraft.items.Kineticator;
import evilcraft.items.KineticatorConfig;
import evilcraft.items.LightningGrenade;
import evilcraft.items.LightningGrenadeConfig;
import evilcraft.items.MaceOfDistortion;
import evilcraft.items.MaceOfDistortionConfig;
import evilcraft.items.PotentiaSphere;
import evilcraft.items.PotentiaSphereConfig;
import evilcraft.items.VengeanceFocus;
import evilcraft.items.VengeanceFocusConfig;
import evilcraft.items.VengeancePickaxe;
import evilcraft.items.VengeancePickaxeConfig;
import evilcraft.items.VengeanceRing;
import evilcraft.items.VengeanceRingConfig;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;
import evilcraft.items.WeatherContainerConfig;

/**
 * Holder class of all the recipes.
 * @author rubensworks
 *
 */
public class Recipes {

    /**
     * The extra buckets that are added with this mod.
     */
    public static Map<Item, FluidStack> BUCKETS = new HashMap<Item, FluidStack>();

    /**
     * Register all the recipes of this mod.
     */
    public static void registerRecipes() {        
        // 9 DarkGems -> 1 DarkBlock
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkBlockConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(DarkBlock.getInstance(), true,
                    new Object[]{
                "GGG",
                "GGG",
                "GGG",
                Character.valueOf('G'), DarkGemConfig._instance.getOreDictionaryId()
            }
                    ));
        }
        // 1 DarkBlock -> 9 DarkGems
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkBlockConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(DarkGem.getInstance(), 9),
                    new ItemStack(DarkBlock.getInstance())
                    ));
        }
        // Weather Container
        if(Configs.isEnabled(WeatherContainerConfig.class) && Configs.isEnabled(DarkPowerGemConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(WeatherContainer.getInstance()), true,
                    new Object[]{
                " G ",
                " P ",
                " S ",
                'G', new ItemStack(DarkPowerGem.getInstance()),
                'P', new ItemStack(Items.glass_bottle),
                'S', new ItemStack(Items.sugar)
            }
                    ));
        }
        // Blood Pearl of Teleportation
        if(Configs.isEnabled(BloodPearlOfTeleportationConfig.class) && Configs.isEnabled(DarkPowerGemConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BloodPearlOfTeleportation.getInstance()), true,
                    new Object[]{
                "EGE",
                "GEG",
                "EGE",
                'G', new ItemStack(DarkPowerGem.getInstance()),
                'E', new ItemStack(Items.ender_pearl)
            }
                    ));
        }
        // Blood Infusion Core
        if(Configs.isEnabled(BloodInfusionCoreConfig.class) && Configs.isEnabled(HardenedBloodShardConfig.class) && Configs.isEnabled(DarkPowerGemConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BloodInfusionCore.getInstance()), true,
                    new Object[]{
                "SSS",
                "SGS",
                "SSS",
                'S', new ItemStack(HardenedBloodShard.getInstance()),
                'G', new ItemStack(DarkPowerGem.getInstance())
            }
                    ));
        }
        // Blood Infuser
        if(Configs.isEnabled(BloodInfusionCoreConfig.class) && Configs.isEnabled(BloodInfuserConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BloodInfuser.getInstance()), true, 
                    new Object[]{
                "CCC",
                "CIC",
                "CCC",
                'C', Reference.DICT_COBBLESTONE,
                'I', new ItemStack(BloodInfusionCore.getInstance())
            }
                    ));
        }
        // Blood Chest
        if(Configs.isEnabled(BloodInfusionCoreConfig.class) && Configs.isEnabled(BloodChestConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BloodChest.getInstance()), true, 
                    new Object[]{
                "PPP",
                "PIP",
                "PPP",
                'P', Reference.DICT_WOODPLANK,
                'I', new ItemStack(BloodInfusionCore.getInstance())
            }
                    ));
        }
        // 1 Undead Log -> 4 Undead Planks
        if(Configs.isEnabled(UndeadLogConfig.class) && Configs.isEnabled(UndeadPlankConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(UndeadPlank.getInstance(), 4),
                    new ItemStack(UndeadLog.getInstance())
                    ));
        }
        // Dark Stick
        if(Configs.isEnabled(UndeadPlankConfig.class) && Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkStickConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(DarkStick.getInstance()), true, 
                    new Object[]{
                " G ",
                " P ",
                " P ",
                'G', new ItemStack(DarkGem.getInstance()),
                'P', new ItemStack(UndeadPlank.getInstance())
            }
                    ));
        }
        // Poison Bucket
        if(Configs.isEnabled(BucketPoisonConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BucketPoison.getInstance()),
                    Reference.DICT_MATERIALPOISONOUS,
                    Reference.DICT_MATERIALPOISONOUS,
                    Reference.DICT_MATERIALPOISONOUS,
                    Reference.DICT_MATERIALPOISONOUS,
                    new ItemStack(Items.water_bucket),
                    new ItemStack(Items.bucket)
                    ));
        }
        // Poisonous potato
        if(Configs.isEnabled(BucketPoisonConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.poisonous_potato),
                    new ItemStack(Items.potato),
                    new ItemStack(BucketPoison.getInstance())
                    ));
        }
        // Poisontip enchant
        if(Configs.isEnabled(BucketPoisonConfig.class) && Configs.isEnabled(EnchantmentPoisonTipConfig.class)) {
            ItemStack poisonTipEnchant = new ItemStack(Items.enchanted_book);
            Enchantment enchant = EnchantmentPoisonTip.getInstance();
            Items.enchanted_book.addEnchantment(poisonTipEnchant, new EnchantmentData(enchant, enchant.getMinLevel()));
            GameRegistry.addRecipe(new ShapelessOreRecipe(poisonTipEnchant,
                    new ItemStack(BucketPoison.getInstance()),
                    new ItemStack(Items.book)
                    ));
        }
        // Potion of poison
        if(Configs.isEnabled(BucketPoisonConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.potionitem, 1, 8196),
                    new ItemStack(BucketPoison.getInstance()),
                    new ItemStack(Items.glass_bottle)
                    ));
        }
        // Obscured glass
        if(Configs.isEnabled(ObscuredGlassConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ObscuredGlass.getInstance(), 8), true, 
                    new Object[]{
                "GGG",
                "GDG",
                "GGG",
                'D', DarkGemConfig._instance.getOreDictionaryId(),
                'G', new ItemStack(Blocks.glass)
            }
                    ));
        }
        // Lightning grenade
        if(Configs.isEnabled(LightningGrenadeConfig.class) && Configs.isEnabled(WeatherContainerConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(LightningGrenade.getInstance(), 8), true,
                    new Object[]{
                "EEE",
                "ELE",
                "EEE",
                'L', new ItemStack(WeatherContainer.getInstance(), 1, WeatherContainerTypes.LIGHTNING.ordinal()),
                'E', new ItemStack(Items.ender_pearl)
            }
                    ));
        }
        // Lightning bomb
        if(Configs.isEnabled(LightningBombConfig.class) && Configs.isEnabled(LightningGrenadeConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(LightningBomb.getInstance()),
                    new ItemStack(LightningGrenade.getInstance()),
                    new ItemStack(Blocks.tnt)
                    ));
        }
        // Blood Containers
        if(Configs.isEnabled(BloodContainerConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BloodContainer.getInstance(), 1, 0), true,
                    new Object[]{
                "DDD",
                "DGD",
                "DDD",
                'D', new ItemStack(DarkGem.getInstance()),
                'G', new ItemStack(Blocks.glass)
            }
                    ));
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
        // Blood Extractor
        if(Configs.isEnabled(BloodContainerConfig.class) && Configs.isEnabled(BloodExtractorConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BloodExtractor.getInstance()), true,
                    new Object[]{
                " I ",
                " S ",
                " C ",
                'C', new ItemStack(BloodContainer.getInstance(), 1, 0),
                'S', new ItemStack(Items.reeds),
                'I', new ItemStack(Items.iron_ingot)
            }
                    ));
        }
        // Potentia Sphere
        if(Configs.isEnabled(PotentiaSphereConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PotentiaSphere.getInstance()), true,
                    new Object[]{
                "RGR",
                "GSG",
                "LGL",
                'S', new ItemStack(Items.slime_ball),
                'R', new ItemStack(Items.redstone),
                'G', new ItemStack(Items.glowstone_dust),
                'L', new ItemStack(Items.dye, 1, 4)
            }
                    ));
        }
        // Purifier
        if(Configs.isEnabled(PurifierConfig.class)
                && Configs.isEnabled(HardenedBloodShardConfig.class)
                && Configs.isEnabled(DarkGemConfig.class)
                && Configs.isEnabled(BloodInfusionCoreConfig.class)
                && Configs.isEnabled(DarkBlockConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Purifier.getInstance()), true,
                    new Object[]{
                "S S",
                "GCG",
                "GBG",
                'S', new ItemStack(HardenedBloodShard.getInstance()),
                'G', new ItemStack(DarkGem.getInstance()),
                'C', new ItemStack(BloodInfusionCore.getInstance()),
                'B', new ItemStack(DarkBlock.getInstance())
            }
                    ));
        }
        // Inverted Potentia
        if(Configs.isEnabled(PotentiaSphereConfig.class) && Configs.isEnabled(InvertedPotentiaConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(InvertedPotentia.getInstance()), true,
                    new Object[]{
                " D ",
                "DSD",
                " D ",
                'S', new ItemStack(PotentiaSphere.getInstance()),
                'D', new ItemStack(DarkGem.getInstance()),
            }
                    ));
        }
        // Mace of Distortion
        if(Configs.isEnabled(MaceOfDistortionConfig.class) && Configs.isEnabled(DarkStickConfig.class) && Configs.isEnabled(DarkPowerGemConfig.class)
                && Configs.isEnabled(InvertedPotentiaConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MaceOfDistortion.getInstance()), true,
                    new Object[]{
                " PI",
                " SP",
                "S  ",
                'S', new ItemStack(DarkStick.getInstance()),
                'P', new ItemStack(DarkPowerGem.getInstance()),
                'I', new ItemStack(InvertedPotentia.getInstance(), 1, InvertedPotentia.EMPOWERED_META),
            }
                    ));
        }
        // Kineticator
        if(Configs.isEnabled(KineticatorConfig.class) && Configs.isEnabled(DarkStickConfig.class) && Configs.isEnabled(BloodInfusionCoreConfig.class)
                && Configs.isEnabled(InvertedPotentiaConfig.class)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Kineticator.getInstance()), true,
                    new Object[]{
                " BS",
                "GIG",
                "SD ",
                'S', new ItemStack(DarkStick.getInstance()),
                'B', new ItemStack(BloodInfusionCore.getInstance()),
                'I', new ItemStack(InvertedPotentia.getInstance(), 1, InvertedPotentia.EMPOWERED_META),
                'G', new ItemStack(Items.gold_ingot),
                'D', new ItemStack(Items.diamond),
            }
                    ));
        }
        // Vengeance Pickaxe
        if(Configs.isEnabled(DarkStickConfig.class) && Configs.isEnabled(VengeancePickaxeConfig.class) && Configs.isEnabled(HardenedBloodShardConfig.class)) {
            ItemStack pickaxe = VengeancePickaxe.createCraftingResult();
        	GameRegistry.addRecipe(new ShapedOreRecipe(pickaxe, true,
                    new Object[]{
                "HDH",
                "DSD",
                " S ",
                'H', new ItemStack(HardenedBloodShard.getInstance()),
                'D', new ItemStack(Items.diamond),
                'S', new ItemStack(DarkStick.getInstance())
            }
                    ));
        }
        // Burning GemStone
        if(Configs.isEnabled(BurningGemStoneConfig.class) && Configs.isEnabled(DarkBlockConfig.class)) {
        	GameRegistry.addSmelting(DarkBlock.getInstance(),
        			new ItemStack(BurningGemStone.getInstance()), 50);
        }
        // Dark Brick
        if(Configs.isEnabled(DarkBrickConfig.class) && Configs.isEnabled(DarkBlockConfig.class)) {
        	GameRegistry.addRecipe(new ShapelessOreRecipe(DarkBrick.getInstance(),
        			new Object[] {
        		DarkBlock.getInstance(), DarkBlock.getInstance(),
        		DarkBlock.getInstance(), DarkBlock.getInstance()
        	}
        			));
        }
        // Spirit furnace
        if(Configs.isEnabled(DarkBloodBrickConfig.class)
        		&& Configs.isEnabled(SpiritFurnaceConfig.class)
        		&& Configs.isEnabled(BloodInfuserConfig.class)) {
        	GameRegistry.addRecipe(new ShapedOreRecipe(SpiritFurnace.getInstance(), true,
                    new Object[]{
                "BBB",
                "BIB",
                "BBB",
                'I', new ItemStack(BloodInfusionCore.getInstance()),
                'B', new ItemStack(DarkBloodBrick.getInstance())
            }
                    ));
        }
        // Vengeance ring
        if(Configs.isEnabled(VengeanceRingConfig.class)
        		&& Configs.isEnabled(DarkGemCrushedConfig.class)) {
        	GameRegistry.addRecipe(new ShapedOreRecipe(VengeanceRing.getInstance(), true,
                    new Object[]{
                "DID",
                "I I",
                "DID",
                'I', new ItemStack(Items.iron_ingot),
                'D', new ItemStack(DarkGemCrushed.getInstance())
            }
                    ));
        }
        // Vengeance focus
        if(Configs.isEnabled(VengeanceRingConfig.class)
        		&& Configs.isEnabled(DarkGemCrushedConfig.class)
        		&& Configs.isEnabled(VengeanceFocusConfig.class)) {
        	GameRegistry.addRecipe(new ShapedOreRecipe(VengeanceFocus.getInstance(), true,
                    new Object[]{
        		"DID",
                "IRI",
                "DID",
                'I', new ItemStack(Items.iron_ingot),
                'D', new ItemStack(DarkGemCrushed.getInstance()),
                'R', new ItemStack(VengeanceRing.getInstance())
            }
                    ));
        }
        // Box of eternal closure
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)
        		&& Configs.isEnabled(DarkGemCrushedConfig.class)) {
        	ItemStack potion = new ItemStack(Items.potionitem, 1, 8264);
        	ItemStack box = new ItemStack(BoxOfEternalClosure.getInstance());
        	BoxOfEternalClosure.setVengeanceSwarmContent(box);
        	GameRegistry.addRecipe(new ShapedOreRecipe(box, true,
                    new Object[]{
        		"DDD",
                "PCP",
                "DDD",
                'C', new ItemStack(Blocks.ender_chest),
                'D', new ItemStack(DarkGemCrushed.getInstance()),
                'P', potion
            }
                    ));
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
        
        if (Configs.isEnabled(EnvironmentalAccumulatorConfig.class) && Configs.isEnabled(WeatherContainerConfig.class)) {
            EnvironmentalAccumulatorRecipe recipe = null;
            EnvironmentalAccumulatorResult result = null;
            
            // Add the different weather container recipes
            ItemStack emptyContainer = WeatherContainer.createItemStack(WeatherContainerTypes.EMPTY, 1);
            WeatherType[] inputs = {WeatherType.CLEAR, WeatherType.RAIN, WeatherType.LIGHTNING};
            WeatherType[] outputs = {WeatherType.RAIN, WeatherType.CLEAR, WeatherType.RAIN};
            
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
