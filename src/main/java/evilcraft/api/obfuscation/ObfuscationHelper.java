package evilcraft.api.obfuscation;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * Helper for getting private fields or methods.
 * @author rubensworks
 *
 */
public class ObfuscationHelper {

	/**
	 * Get the private 'particleTextures' field from {@link net.minecraft.client.particle.EffectRenderer}.
	 * @return The private 'particleTextures' field.
	 */
	public static ResourceLocation getParticleTexture() {
		return ReflectionHelper.getPrivateValue(EffectRenderer.class, null, ObfuscationData.PARTICLE_TEXTURES);
	}
	
}
