package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.core.degradation.StochasticDegradationEffect;


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
        RandomSource random = degradable.getDegradationWorld().random;
        Level world = degradable.getDegradationWorld();
        for(Entity entity : degradable.getAreaEntities()) {
            if(entity instanceof Player) {
                world.playSound((Player) entity, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLAZE_AMBIENT, SoundSource.AMBIENT, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                world.playSound((Player) entity, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.AMBIENT, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            }
        }
    }

}
