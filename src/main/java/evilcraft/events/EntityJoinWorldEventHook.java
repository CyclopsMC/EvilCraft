package evilcraft.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.render.particle.EntityFireShootFX;

/**
 * Event hook for {@link EntityJoinWorldEvent}.
 * @author rubensworks
 *
 */
public class EntityJoinWorldEventHook {
    
    // List of players that have a ring of fire
    private static final List<String> ALLOW_RING = new ArrayList<String>();
    static {
        ALLOW_RING.add("kroeserr");
        ALLOW_RING.add("_EeB_");
        ALLOW_RING.add("JonaBrackenwood");
    }
    private static double RING_AREA = 0.9F;

    /**
     * When an entity join world event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBonemeal(EntityJoinWorldEvent event) {
        fireRing(event);
    }
    
    private void fireRing(EntityJoinWorldEvent event) {
        if(event.world.isRemote
                && event.entity instanceof EntityPlayer
                && ALLOW_RING.contains(((EntityPlayer)event.entity).getDisplayName())) {
            showFireRing(event.entity);
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void showFireRing(Entity entity) {
        double area = RING_AREA;
        World world = entity.worldObj;
        int points = 40;
        for(double point = -points; point <= points; point++) {
            double u = 2 * Math.PI * (point / points);

            double xOffset = Math.cos(u) * area;
            double yOffset = 0F;
            double zOffset = Math.sin(u) * area;

            double xCoord = entity.posX;
            double yCoord = entity.posY;
            double zCoord = entity.posZ;

            double particleX = xCoord + xOffset + world.rand.nextFloat() / 5;
            double particleY = yCoord + yOffset + world.rand.nextFloat() / 5;
            double particleZ = zCoord + zOffset + world.rand.nextFloat() / 5;

            float particleMotionX = (float)xOffset / 5;
            float particleMotionY = 0.25F;
            float particleMotionZ = (float)zOffset / 5;

            if(world.rand.nextInt(20) == 0) {
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityFireShootFX(world, particleX, particleY, particleZ,
                                particleMotionX, particleMotionY, particleMotionZ, 0.1F)
                        );
            } else {
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new EntityFlameFX(world, particleX, particleY, particleZ, 0, 0, 0)
                        );
            }
        }
    }
    
}
