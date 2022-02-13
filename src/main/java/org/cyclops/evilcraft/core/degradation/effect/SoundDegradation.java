package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.core.degradation.StochasticDegradationEffect;

import java.util.Random;

/**
 * Client-side degradation effect that will play creepy sounds.
 * @author rubensworks
 *
 */
public class SoundDegradation extends StochasticDegradationEffect {
    
    private static final double CHANCE = 0.1D;
    
    public SoundDegradation(DegradationEffectConfig eConfig) {
        super(eConfig, CHANCE);
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        Random random = degradable.getDegradationWorld().random;
        World world = degradable.getDegradationWorld();
        for(Entity entity : degradable.getAreaEntities()) {
            if(entity instanceof PlayerEntity) {
                world.playSound((PlayerEntity) entity, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLAZE_AMBIENT, SoundCategory.AMBIENT, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                world.playSound((PlayerEntity) entity, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDER_DRAGON_FLAP, SoundCategory.AMBIENT, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            }
        }
    }
    
}
