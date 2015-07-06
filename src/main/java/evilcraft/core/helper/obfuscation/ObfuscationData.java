package evilcraft.core.helper.obfuscation;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Entries used for getting private fields and methods by using it in
 * {@link ReflectionHelper#getPrivateValue(Class, Object, String...)}.
 * These MCP mappings should be updated with every MC update!
 * @author rubensworks
 * TODO !!!
 *
 */
public class ObfuscationData {
	
	/**
	 * Field from net.minecraft.client.particle.EffectRenderer.
	 */
	public static final String[] PARTICLE_TEXTURES = new String[] { "particleTextures", "field_110737_b", "b" };
	
	/**
	 * Field from net.minecraft.entity.EntityLivingBase.
	 */
	public static final String[] ENTITYLIVINGBASE_RECENTLYHIT = new String[] { "recentlyHit", "field_70718_bc", "bc" };
	
	/**
	 * Method from net.minecraft.entity.EntityLivingBase.
	 */
	public static final String[] ENTITYLIVINGBASE_GETDEATHSOUND = new String[] { "getDeathSound", "func_70673_aS", "aS" };
	
	/**
	 * Method from net.minecraft.entity.EntityLiving.
	 */
	public static final String[] ENTITYLIVING_GETLIVINGSOUND = new String[] { "getLivingSound", "func_70639_aQ", "aQ" };
	
	/**
	 * Field from net.minecraft.entity.player.EntityPlayer.
	 */
	public static final String[] ENTITYPLAYER_ITEMINUSECOUNT = new String[] { "itemInUseCount", "field_71072_f", "f" };
	
	/**
	 * Field from net.minecraft.entity.player.EntityPlayer.
	 */
	public static final String[] ENTITYPLAYER_ITEMINUSE = new String[] { "itemInUse", "field_71074_e", "e" };
	
	/**
	 * Field from net.minecraft.client.gui.GuiMainMenu.
	 */
	public static final String[] GUIMAINMENU_TITLEPANORAMAPATHS = new String[] { "titlePanoramaPaths", "field_73978_o", "o" };
	
	/**
	 * Field from net.minecraft.potion.PotionEffect.
	 */
	public static final String[] POTIONEFFECT_DURATION = new String[] { "duration", "field_76460_b", "b" };
	
	/**
	 * Method from net.minecraft.entity.EntityLivingBase.
	 */
	public static final String[] ENTITYLIVINGBASE_ONCHANGEDPOTIONEFFECT = new String[] { "onChangedPotionEffect", "func_70695_b", "b" };
	
	/**
	 * Field from net.minecraft.entity.EntityList.
	 */
	public static final String[] ENTITYLIST_CLASSTOID = new String[] { "classToIDMapping", "field_75624_e", "e" };
	
	/**
	 * Field from net.minecraft.potion.Potion.
	 */
	public static final String[] POTION_ISBADEFFECT = new String[] { "isBadEffect", "field_76418_K", "K" };

    /**
     * Field from net.minecraft.potion.Potion.
     */
    public static final String[] POTION_POTIONTYPES = new String[] { "potionTypes", "field_76425_a", "a" };

    /**
     * Field from net.minecraft.world.biome.BiomeGenBase.
     */
    public static final String[] BIOME_ENABLERAIN = new String[] { "enableRain", "field_76765_S", "S" };
    /**
     * Field from net.minecraftforge.oredict.ShapedOreRecipe.
     */
    public static final String[] SHAPEDORERECIPE_WIDTH = new String[] { "width" };
    /**
     * Field from net.minecraftforge.oredict.ShapedOreRecipe.
     */
    public static final String[] SHAPEDORERECIPE_HEIGHT = new String[] { "height" };
	
}
