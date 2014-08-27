package evilcraft.api.helpers.obfuscation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.ReflectionHelper;
import evilcraft.api.helpers.RenderHelpers;

/**
 * Helper for getting private fields or methods.
 * @author rubensworks
 *
 */
public class ObfuscationHelpers {

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
	 * Get the private 'itemInUse' field from {@link net.minecraft.entity.player.EntityPlayer}.
	 * @param player The player.
	 * @return itemInUse
	 */
	public static ItemStack getItemInUse(EntityPlayer player) {
		return ReflectionHelper.getPrivateValue(EntityPlayer.class, player, ObfuscationData.ENTITYPLAYER_ITEMINUSE);
	}
	
	/**
	 * Set the private static 'titlePanoramaPaths' field from @link{net.minecraft.client.gui.GuiMainMenu}
	 * @param titlePanoramaPaths The panorama path.
	 */
	public static void setTitlePanoramaPaths(ResourceLocation[] titlePanoramaPaths) {
		Field field = ReflectionHelper.findField(GuiMainMenu.class, ObfuscationData.GUIMAINMENU_TITLEPANORAMAPATHS);
		
		Field modifiersField;
		try {
			modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
	        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

	        field.setAccessible(true);
	        field.set(null, titlePanoramaPaths);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}        
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the renderFace method for the given side in {@link net.minecraft.client.renderer.RenderBlocks}.
	 * @param renderer The block renderer.
	 * @param direction The side.
	 * @return The method.
	 */
	public static Method getRenderFaceMethod(RenderBlocks renderer, ForgeDirection direction) {
		Method method = ReflectionHelper.findMethod(RenderBlocks.class, renderer,
				ObfuscationData.RENDERBLOCKS_RENDERFACE.get(direction), Block.class, double.class,
				double.class, double.class, IIcon.class);
		return method;
	}
	
	/**
	 * Set a private 'uvRotate' field from {@link net.minecraft.client.renderer.RenderBlocks}
	 * depending on which direction you specified.
	 * Note that this does not contain the MC side number bug fix, for this, please use {@link RenderHelpers}.
	 * @param renderer The block renderer.
	 * @param direction The side.
	 * @param rotation The rotation to set.
	 */
	public static void setUVRotate(RenderBlocks renderer, ForgeDirection direction, int rotation) {
		ReflectionHelper.setPrivateValue(RenderBlocks.class, renderer, rotation,
				ObfuscationData.RENDERBLOCKS_UVROTATE.get(direction));
	}
	
}
