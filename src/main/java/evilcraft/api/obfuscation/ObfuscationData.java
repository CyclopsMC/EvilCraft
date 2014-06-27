package evilcraft.api.obfuscation;

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
		
}
