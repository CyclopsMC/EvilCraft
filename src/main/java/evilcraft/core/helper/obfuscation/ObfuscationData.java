package evilcraft.core.helper.obfuscation;

import java.util.Map;

import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * Entries used for getting private fields and methods by using it in
 * {@link ReflectionHelper#getPrivateValue(Class, Object, String...)}.
 * These MCP mappings should be updated with every MC update!
 * @author rubensworks
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
	 * Methods from net.minecraft.client.renderer.RenderBlocks.
	 */
	public static final Map<ForgeDirection, String[]> RENDERBLOCKS_RENDERFACE = Maps.newHashMap();
	static {
		RENDERBLOCKS_RENDERFACE.put(ForgeDirection.DOWN, new String[] { "renderFaceYNeg", "func_147768_a", "a" });
		RENDERBLOCKS_RENDERFACE.put(ForgeDirection.UP, new String[] { "renderFaceYPos", "func_147806_b", "b" });
		RENDERBLOCKS_RENDERFACE.put(ForgeDirection.NORTH, new String[] { "renderFaceZPos", "func_147734_d", "d" });
		RENDERBLOCKS_RENDERFACE.put(ForgeDirection.EAST, new String[] { "renderFaceXPos", "func_147764_f", "f" });
		RENDERBLOCKS_RENDERFACE.put(ForgeDirection.SOUTH, new String[] { "renderFaceZNeg", "func_147761_c", "c" });
		RENDERBLOCKS_RENDERFACE.put(ForgeDirection.WEST, new String[] { "renderFaceXNeg", "func_147798_e", "e" });
	}
	
	/**
	 * Fields from net.minecraft.client.renderer.RenderBlocks.
	 */
	public static final Map<ForgeDirection, String[]> RENDERBLOCKS_UVROTATE = Maps.newHashMap();
	static {
		RENDERBLOCKS_UVROTATE.put(ForgeDirection.DOWN, new String[] { "uvRotateBottom", "field_147865_v", "v" });
		RENDERBLOCKS_UVROTATE.put(ForgeDirection.UP, new String[] { "uvRotateTop", "field_147867_u", "u" });
		RENDERBLOCKS_UVROTATE.put(ForgeDirection.NORTH, new String[] { "uvRotateEast", "field_147875_q", "q" });
		RENDERBLOCKS_UVROTATE.put(ForgeDirection.EAST, new String[] { "uvRotateSouth", "field_147871_s", "s" });
		RENDERBLOCKS_UVROTATE.put(ForgeDirection.SOUTH, new String[] { "uvRotateWest", "field_147873_r", "r" });
		RENDERBLOCKS_UVROTATE.put(ForgeDirection.WEST, new String[] { "uvRotateNorth", "field_147869_t", "t" });
	}
	
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
	
}
