package org.cyclops.evilcraft.client.particle;

import cpw.mods.fml.client.FMLClientHandler;
import evilcraft.Reference;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * An extended {@link EntityExplodeFX}
 * @author rubensworks
 *
 */
public class ExtendedEntityExplodeFX extends EntityExplodeFX {

	/**
	 * Make a new instance.
	 * @param world The world.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param motionX The X motion speed.
	 * @param motionY The Y motion speed.
	 * @param motionZ The Z motion speed.
	 * @param red Red tint.
	 * @param green Green tint.
	 * @param blue Blue tint.
	 * @param alpha The particle alpha.
	 */
	public ExtendedEntityExplodeFX(World world, double x, double y, double z,
								   double motionX, double motionY, double motionZ,
								   float red, float green, float blue, float alpha) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleAlpha = alpha;
	}

}
