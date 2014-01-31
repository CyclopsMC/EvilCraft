package evilcraft.render.entity;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.render.ModelRenderLiving;
import evilcraft.entities.monster.Werewolf;

/**
 * Renderer for a werewolf
 * 
 * @author rubensworks
 *
 */
public class RenderWerewolf extends ModelRenderLiving {
    
	public RenderWerewolf(ExtendedConfig config, ModelBase model, float par2) {
	    super(config, model, par2);
	}
	
	public void renderWerewolf(Werewolf werewolf, double par2, double par4, double par6, float par8, float par9) {
	    super.doRenderLiving(werewolf, par2, par4, par6, par8, par9);
    }

	@Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
        this.renderWerewolf((Werewolf)par1EntityLiving, par2, par4, par6, par8, par9);
    }

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderWerewolf((Werewolf)par1Entity, par2, par4, par6, par8, par9);
    }

}
