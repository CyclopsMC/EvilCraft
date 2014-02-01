package evilcraft.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

import org.lwjgl.opengl.GL11;

import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.render.ModelRenderLiving;
import evilcraft.entities.monster.PoisonousLibelle;

/**
 * Renderer for a libelle
 * 
 * @author rubensworks
 *
 */
public class RenderPoisonousLibelle extends ModelRenderLiving {
    
	public RenderPoisonousLibelle(ExtendedConfig config, ModelBase model, float par2) {
	    super(config, model, par2);
	}
	
	public void renderLibelle(PoisonousLibelle libelle, double par2, double par4, double par6, float par8, float par9) {
	    super.doRenderLiving(libelle, par2, par4, par6, par8, par9);
    }

	@Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        this.renderLibelle((PoisonousLibelle)par1EntityLiving, par2, par4, par6, par8, par9);
    }

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderLibelle((PoisonousLibelle)par1Entity, par2, par4, par6, par8, par9);
    }

}
