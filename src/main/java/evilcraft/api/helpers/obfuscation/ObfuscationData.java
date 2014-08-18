package evilcraft.api.helpers.obfuscation;

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
	
}
