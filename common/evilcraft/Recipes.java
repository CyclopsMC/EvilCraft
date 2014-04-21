package evilcraft;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.blocks.BloodChest;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.blocks.DarkBlock;
import evilcraft.blocks.DarkBlockConfig;
import evilcraft.blocks.LightningBomb;
import evilcraft.blocks.LightningBombConfig;
import evilcraft.blocks.ObscuredGlass;
import evilcraft.blocks.ObscuredGlassConfig;
import evilcraft.blocks.Purifier;
import evilcraft.blocks.PurifierConfig;
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
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.DarkPowerGem;
import evilcraft.items.DarkPowerGemConfig;
import evilcraft.items.DarkStick;
import evilcraft.items.DarkStickConfig;
import evilcraft.items.HardenedBloodShard;
import evilcraft.items.HardenedBloodShardConfig;
import evilcraft.items.InvertedPotentia;
import evilcraft.items.InvertedPotentiaConfig;
import evilcraft.items.LightningGrenade;
import evilcraft.items.LightningGrenadeConfig;
import evilcraft.items.MaceOfDistortion;
import evilcraft.items.MaceOfDistortionConfig;
import evilcraft.items.PotentiaSphere;
import evilcraft.items.PotentiaSphereConfig;
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
                'P', new ItemStack(Item.glassBottle),
                'S', new ItemStack(Item.sugar)
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
                'E', new ItemStack(Item.enderPearl)
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
                    new ItemStack(Item.bucketWater),
                    new ItemStack(Item.bucketEmpty)
                    ));
        }
        // Poisonous potato
        if(Configs.isEnabled(BucketPoisonConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Item.poisonousPotato),
                    new ItemStack(Item.potato),
                    new ItemStack(BucketPoison.getInstance())
                    ));
        }
        // Poisontip enchant
        if(Configs.isEnabled(BucketPoisonConfig.class) && Configs.isEnabled(EnchantmentPoisonTipConfig.class)) {
            ItemStack poisonTipEnchant = new ItemStack(Item.enchantedBook);
            Enchantment enchant = EnchantmentPoisonTip.getInstance();
            Item.enchantedBook.addEnchantment(poisonTipEnchant, new EnchantmentData(enchant, enchant.getMinLevel()));
            GameRegistry.addRecipe(new ShapelessOreRecipe(poisonTipEnchant,
                    new ItemStack(BucketPoison.getInstance()),
                    new ItemStack(Item.book)
                    ));
        }
        // Potion of poison
        if(Configs.isEnabled(BucketPoisonConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Item.potion, 1, 8196),
                    new ItemStack(BucketPoison.getInstance()),
                    new ItemStack(Item.glassBottle)
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
                'G', new ItemStack(Block.glass)
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
                'E', new ItemStack(Item.enderPearl)
            }
                    ));
        }
        // Lightning bomb
        if(Configs.isEnabled(LightningBombConfig.class) && Configs.isEnabled(LightningGrenadeConfig.class)) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(LightningBomb.getInstance()),
                    new ItemStack(LightningGrenade.getInstance()),
                    new ItemStack(Block.tnt)
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
                'G', new ItemStack(Block.glass)
            }
                    ));
            for(int i = 1; i < BloodContainerConfig.getContainerLevels(); i++) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BloodContainer.getInstance(), 1, i),
                        new ItemStack(BloodContainer.getInstance(), 1, i - 1),
                        new ItemStack(BloodContainer.getInstance(), 1, i - 1)
                        ));
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
                'S', new ItemStack(Item.reed),
                'I', new ItemStack(Item.ingotIron)
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
                'S', new ItemStack(Item.slimeBall),
                'R', new ItemStack(Item.redstone),
                'G', new ItemStack(Item.glowstone),
                'L', new ItemStack(Item.dyePowder, 1, 4)
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

        registerCustomRecipes();
    }

    private static void registerCustomRecipes() {
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

        if(Configs.isEnabled(UndeadSaplingConfig.class)) {
            CustomRecipeRegistry.put(new CustomRecipe(
                    new ItemStack(Block.deadBush),
                    new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME * 2),
                    BloodInfuser.getInstance(),
                    200
                    ),
                    new ItemStack(UndeadSapling.getInstance()
                            ));
        }
        
        if(Configs.isEnabled(BlookConfig.class)) {
            CustomRecipeRegistry.put(new CustomRecipe(
                    new ItemStack(Item.book),
                    new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME / 2),
                    BloodInfuser.getInstance(),
                    500
                    ),
                    new ItemStack(Blook.getInstance()
                            ));
        }
        
        if(Configs.isEnabled(PotentiaSphereConfig.class) && PotentiaSphereConfig.enderPearlRecipe) {
            CustomRecipeRegistry.put(new CustomRecipe(
                    new ItemStack(PotentiaSphere.getInstance()),
                    new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME * 2),
                    BloodInfuser.getInstance(),
                    1000
                    ),
                    new ItemStack(Item.enderPearl
                            ));
        }
    }
    
}
