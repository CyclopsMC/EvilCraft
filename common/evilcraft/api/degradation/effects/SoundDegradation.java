package evilcraft.api.degradation.effects;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.StochasticDegradationEffect;

/**
 * Client-side degradation effect that will play creepy sounds.
 * @author rubensworks
 *
 */
public class SoundDegradation extends StochasticDegradationEffect {

private static final double CHANCE = 0.1D;
    
    /**
     * Make a new instance.
     */
    public SoundDegradation() {
        super(CHANCE);
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        Random random = degradable.getWorld().rand;
        World world = degradable.getWorld();
        for(Entity entity : degradable.getAreaEntities()) {
            if(entity instanceof EntityPlayer) {
                world.playSoundAtEntity((EntityPlayer) entity, "mob.blaze.breathe", 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                world.playSoundAtEntity((EntityPlayer) entity, "mob.enderdragon.wings", 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            }
        }
    }
    
}
