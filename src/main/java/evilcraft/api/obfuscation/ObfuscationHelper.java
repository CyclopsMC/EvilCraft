package evilcraft.api.obfuscation;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLivingBase;
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
	
	/**
	 * set the private 'recentlyHit' field from {@link net.minecraft.entity.EntityLivingBase}.
	 * @param entity The entity instance.
	 * @param recentlyHit The recently hit value to set.
	 */
	public static void setRecentlyHit(EntityLivingBase entity, int recentlyHit) {
		ReflectionHelper.setPrivateValue(EntityLivingBase.class, entity, recentlyHit, ObfuscationData.ENTITYLIVINGBASE_RECENTLYHIT);
	}
	
}
