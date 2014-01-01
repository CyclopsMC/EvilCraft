package evilcraft.events;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import evilcraft.ExtendedDamageSource;
import evilcraft.blocks.ExcrementPile;
import evilcraft.blocks.ExcrementPileConfig;

public class PlaySoundAtEntityEventHook {
    
    private static final int CHANCE_DROP_EXCREMENT = 500; // Real chance is 1/CHANCE_DROP_EXCREMENT
    private static final int CHANCE_DIE_WITHOUT_ANY_REASON = 1000000; // Real chance is 1/CHANCE_DIE_WITHOUT_ANY_REASON

    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event) {
        dropExcrement(event);
        dieWithoutAnyReason(event);
    }
    
    private void dropExcrement(PlaySoundAtEntityEvent event) {
        if(event.entity instanceof EntityAnimal) {
            EntityAnimal entity = (EntityAnimal) event.entity;
            World world = entity.worldObj;
            if(world.rand.nextInt(CHANCE_DROP_EXCREMENT) == 0) {
                int x = MathHelper.floor_double(entity.posX);
                int y = MathHelper.floor_double(entity.posY);
                int z = MathHelper.floor_double(entity.posZ);
                if(world.getBlockId(x, y, z) == 0 && world.isBlockNormalCube(x, y - 1, z)) {
                    world.setBlock(x, y, z, ExcrementPileConfig._instance.ID);
                } else if (world.getBlockId(x, y, z) == ExcrementPileConfig._instance.ID) {
                    ExcrementPile.heightenPileAt(world, x, y, z);
                }
            }
        }
    }
    
    private void dieWithoutAnyReason(PlaySoundAtEntityEvent event) {
        if(event.entity instanceof EntityPlayer && event.entity.worldObj.rand.nextInt(CHANCE_DIE_WITHOUT_ANY_REASON) == 0) {
            EntityPlayer entity = (EntityPlayer) event.entity;
            entity.attackEntityFrom(ExtendedDamageSource.dieWithoutAnyReason, Float.MAX_VALUE);
        }
    }
    
}
