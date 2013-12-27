package evilcraft.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

public class RenderLivingEventHook {
    
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void RenderLivingPre(RenderLivingEvent.Pre event) {
        EntityLivingBase entity = event.entity;
        
        float par9 = 1.0F;
        float par3 = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, par9);
        GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
        
        String s = EnumChatFormatting.func_110646_a(entity.getEntityName());
        
        if ((s.equals("kroeserr")) && (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).getHideCape()))
        {
            GL11.glTranslatef(0.0F, entity.height + 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        }
    }
    
    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    private float interpolateRotation(float par1, float par2, float par3)
    {
        float f3;

        for (f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F)
        {
            ;
        }

        while (f3 >= 180.0F)
        {
            f3 -= 360.0F;
        }

        return par1 + par3 * f3;
    }
}
