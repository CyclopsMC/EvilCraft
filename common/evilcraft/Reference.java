package evilcraft;

/**
 * Class that can hold basic static things that are better not hard-coded
 * like mod details, texture paths, ID's...
 * @author rubensworks
 *
 */
@SuppressWarnings("javadoc")
public class Reference {
    // Mod info
    public static final String MOD_ID = "evilcraft";
    public static final String MOD_NAME = "EvilCraft";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_BUILD_NUMBER = "@BUILD_NUMBER@";
    public static final String MOD_MC_VERSION = "@MC_VERSION@";
    
    public static final String MOD_CHANNEL = MOD_ID;
    
    // Paths
    public static final String TEXTURE_PATH_GUI = "textures/gui/";
    public static final String TEXTURE_PATH_SKINS = "textures/skins/";
    public static final String TEXTURE_PATH_MODELS = "textures/models/";
    public static final String TEXTURE_PATH_ENTITIES = "textures/entities/";
    public static final String TEXTURE_PATH_GUIBACKGROUNDS = "textures/gui/title/background/";
    public static final String TEXTURE_PATH_ITEMS = "textures/items/";
    
 // External locations
    public static final String URL_VERSIONSTATS = "http://rubensworks.net/evilcraft-versionstats/";
    
    // Block ID's
    public static final int BLOCK_BLOODSTAINEDDIRT = 3846;
    public static final int BLOCK_DARKBLOCK = 3847;
    public static final int BLOCK_DARKORE = 3848;
    public static final int BLOCK_DARKOREGLOWING = 3849;
    public static final int BLOCK_EVILBLOCK = 3850;
    public static final int BLOCK_LARGEDOOR = 3851;
    public static final int BLOCK_LIGHTNINGBOMB = 3852;
    public static final int BLOCK_FLUIDBLOCKBLOOD = 3853;
    public static final int BLOCK_BLOODINFUSER = 3854;
    public static final int BLOCK_BLOODYCOBBLESTONE = 3855;
    public static final int BLOCK_HARDENEDBLOOD = 3856;
    public static final int BLOCK_OBSCUREDGLASS = 3857;
    public static final int BLOCK_BLOODCHEST = 3858;
    public static final int BLOCK_ENVIRONMENTAL_ACCUMULATOR = 3859;
    public static final int BLOCK_UNDEADSAPLING = 3860;
    public static final int BLOCK_UNDEADLEAVES = 3861;
    public static final int BLOCK_UNDEADWOOD = 3862;
    public static final int BLOCK_UNDEADPLANK = 3863;
    public static final int BLOCK_FLUIDBLOCKPOISON = 3864;
    public static final int BLOCK_INVISIBLEREDSTONE = 3865;
    public static final int BLOCK_PURIFIER = 3866;
    
    // Item ID's
    public static final int ITEM_BLOODEXTRACTOR = 4000;
    public static final int ITEM_BUCKETBLOOD = 4001;
    public static final int ITEM_CONTAINEDFLUX = 4002;
    public static final int ITEM_DARKGEM = 4003;
    public static final int ITEM_DARKSTICK = 4004;
    public static final int ITEM_LARGEDOOR = 4005;
    public static final int ITEM_LIGHTNINGGRENADE = 4006;
    public static final int ITEM_REDSTONEGRENADE = 4018;
    public static final int ITEM_WEREWOLFBONE = 4007;
    public static final int ITEM_WEREWOLFFLESH = 4008;
    public static final int ITEM_WEATHERCONTAINER = 4008;
    public static final int ITEM_BLOODPEARLOFTELEPORTATION = 4009;
    public static final int ITEM_BROOM = 4010;
    public static final int ITEM_HARDENEDBLOODSHARD = 4011;
    public static final int ITEM_DARKPOWERGEM = 4012;
    public static final int ITEM_BLOODINFUSIONCORE = 4013;
    public static final int ITEM_BLOODCONTAINER = 4014;
    public static final int ITEM_WEREWOLFFUR = 4015;
    public static final int ITEM_POISONSAC = 4016;
    public static final int ITEM_BUCKETPOISON = 4017;
    public static final int ITEM_BLOOK = 4018;
    public static final int ITEM_POTENTIASPHERE = 4019;
    public static final int ITEM_INVERTEDPOTENTIA = 4020;
    public static final int ITEM_MACEOFDISTORTION = 4021;
    public static final int ITEM_KINETICATOR = 4022;
    
    // Enchantment ID's
    public static final int ENCHANTMENT_BREAKING = 101;
    public static final int ENCHANTMENT_LIFESTEALING = 102;
    public static final int ENCHANTMENT_UNUSING = 103;
    public static final int ENCHANTMENT_POISON_TIP = 104;
    public static final int ENCHANTMENT_KNOCKSPEED = 105;
    
    // Entity ID's
    public static final int ENTITY_LIGHTNINGBOMB = 3;
    public static final int ENTITY_LIGHTNINGGRENADE = 4;
    public static final int ENTITY_REDSTONEGRENADE = 9;
    public static final int ENTITY_BROOM = 5;
    public static final int ENTITY_BLOODPEARL = 6;
    public static final int ENTITY_WEATHERCONTAINER = 7;
    
    // MOB ID's
    public static final int MOB_WEREWOLF = 1;
    public static final int MOB_NETHERFISH = 2;
    public static final int MOB_POISONOUSLIBELLE =32;
    
    // GUI ID's
    public static final int GUI_BLOOD_INFUSER = 10;
    public static final int GUI_BLOOD_CHEST = 11;
    
    // OREDICT NAMES
    public static final String DICT_MATERIALPOISONOUS = "materialPoisonous";
    public static final String DICT_MATERIALGLASS = "materialGlass";
    public static final String DICT_MATERIALLEATHER = "materialLeather";
    public static final String DICT_MATERIALBONE = "materialBone";
    public static final String DICT_COBBLESTONE = "cobblestone";
    public static final String DICT_BLOCKSTONE = "blockStone";
    public static final String DICT_OREDARK = "oreDark";
    public static final String DICT_WOODPLANK = "plankWood";
    public static final String DICT_WOODLOG = "logWood";
    public static final String DICT_WOODSTICK = "stickWood";
    public static final String DICT_SAPLINGTREE = "treeSapling";
    public static final String DICT_GEMDARK = "gemDark";
    
    // MOD ID's
    public static final String MOD_FORGE = "Forge";
    public static final String MOD_THERMALEXPANSION = "ThermalExpansion";
    public static final String MOD_BUILDCRAFT_TRANSPORT = "BuildCraft|Transport";
    public static final String MOD_WAILA = "Waila";
    public static final String MOD_FMP = "ForgeMultipart";
    public static final String MOD_FORESTRY = "Forestry";
    
    // Dependencies
    public static final String MOD_DEPENDENCIES = "required-after:" + MOD_FORGE + "@[@FORGE_VERSION@,)"
            + "after:"+MOD_BUILDCRAFT_TRANSPORT
            + ";after:"+Reference.MOD_THERMALEXPANSION;
    
    // Keybinding ID's
    public static final String KEY_FART = "key.evilcraft.fart";
}