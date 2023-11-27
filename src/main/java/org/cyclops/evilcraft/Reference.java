package org.cyclops.evilcraft;

import org.cyclops.cyclopscore.helper.MinecraftHelpers;

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
    public static final String GA_TRACKING_ID = "UA-65307010-2";
    public static final String VERSION_URL = "https://raw.githubusercontent.com/CyclopsMC/Versions/master/" + MinecraftHelpers.getMinecraftVersionMajorMinor() + "/EvilCraft.txt";
    public static final String BOOK_URL = "https://evilcraft.rubensworks.net/book/";

    // Paths
    public static final String TEXTURE_PATH_GUI = "textures/gui/";
    public static final String TEXTURE_PATH_SKINS = "textures/skin/";
    public static final String TEXTURE_PATH_MODELS = "textures/model/";
    public static final String TEXTURE_PATH_ENTITIES = "textures/entity/";
    public static final String TEXTURE_PATH_ITEMS = "textures/item/";
    public static final String TEXTURE_PATH_PARTICLES = "textures/particle/";
    public static final String MODEL_PATH = "models/";

    // MOD ID's
    public static final String MOD_FORGE = "forge";
    public static final String MOD_CYCLOPSCORE = "cyclopscore";
    public static final String MOD_BAUBLES = "baubles";

    // IMC keys
    public static final String IMC_BLACKLIST_VENGEANCESPIRIT = "blacklist_vengeance_spirit";

}
