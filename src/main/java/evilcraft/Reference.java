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
    
    public static final String MOD_CHANNEL = MOD_ID;
    
    // Paths
    public static final String TEXTURE_PATH_GUI = "textures/gui/";
    public static final String TEXTURE_PATH_SKINS = "textures/skins/";
    public static final String TEXTURE_PATH_MODELS = "textures/models/";
    public static final String TEXTURE_PATH_ENTITIES = "textures/entities/";
    public static final String TEXTURE_PATH_GUIBACKGROUNDS = "textures/gui/title/background/";
    public static final String TEXTURE_PATH_ITEMS = "textures/items/";
    
    // Enchantment ID's
    public static final int ENCHANTMENT_BREAKING = 101;
    public static final int ENCHANTMENT_LIFESTEALING = 102;
    public static final int ENCHANTMENT_UNUSING = 103;
    public static final int ENCHANTMENT_POISON_TIP = 104;
    public static final int ENCHANTMENT_KNOCKSPEED = 105;
    
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
    
    // Dependencies
    public static final String MOD_DEPENDENCIES = ""// TODO: "required-after:" + MOD_FORGE + "@[@FORGE_VERSION@,)"
            + "after:"+MOD_BUILDCRAFT_TRANSPORT
            + ";after:"+Reference.MOD_THERMALEXPANSION;
    
    // Keybinding ID's
    // TODO: move this shit
    public static final String KEY_FART = "key.evilcraft.fart";
}