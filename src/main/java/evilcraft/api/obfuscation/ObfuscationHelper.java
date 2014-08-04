package evilcraft.api.obfuscation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	 * Set the private 'recentlyHit' field from {@link net.minecraft.entity.EntityLivingBase}.
	 * @param entity The entity instance.
	 * @param recentlyHit The recently hit value to set.
	 */
	public static void setRecentlyHit(EntityLivingBase entity, int recentlyHit) {
		ReflectionHelper.setPrivateValue(EntityLivingBase.class, entity, recentlyHit, ObfuscationData.ENTITYLIVINGBASE_RECENTLYHIT);
	}
	
	/**
	 * Call the protected getter 'getDeathSound' {@link net.minecraft.entity.EntityLivingBase}.
	 * @param entity The entity instance.
	 * @return The death sound.
	 */
	public static String getDeathSound(EntityLivingBase entity) {
		Method method = ReflectionHelper.findMethod(EntityLivingBase.class, entity, ObfuscationData.ENTITYLIVINGBASE_GETDEATHSOUND);
		try {
			return (String) method.invoke(entity);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Call the protected getter 'getLivingSound' {@link net.minecraft.entity.EntityLiving}.
	 * @param entity The entity instance.
	 * @return The living sound.
	 */
	public static String getLivingSound(EntityLiving entity) {
		Method method = ReflectionHelper.findMethod(EntityLiving.class, entity, ObfuscationData.ENTITYLIVING_GETLIVINGSOUND);
		try {
			return (String) method.invoke(entity);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Set the private 'itemInUseCount' field from {@link net.minecraft.entity.player.EntityPlayer}.
	 * @param player The player.
	 * @return itemInUseCount
	 */
	public static int getItemInUseCount(EntityPlayer player) {
		return ReflectionHelper.getPrivateValue(EntityPlayer.class, player, ObfuscationData.ENTITYPLAYER_ITEMINUSECOUNT);
	}
	
	/**
	 * Set the private 'itemInUse' field from {@link net.minecraft.entity.player.EntityPlayer}.
	 * @param player The player.
	 * @return itemInUse
	 */
	public static ItemStack getItemInUse(EntityPlayer player) {
		return ReflectionHelper.getPrivateValue(EntityPlayer.class, player, ObfuscationData.ENTITYPLAYER_ITEMINUSE);
	}
	
}
