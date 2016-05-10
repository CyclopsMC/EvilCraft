package org.cyclops.evilcraft.core.degradation.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
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

    private static SoundDegradation _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SoundDegradation getInstance() {
        return _instance;
    }
    
    private static final double CHANCE = 0.1D;
    
    public SoundDegradation(ExtendedConfig<DegradationEffectConfig> eConfig) {
        super(eConfig, CHANCE);
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        Random random = degradable.getDegradationWorld().rand;
        World world = degradable.getDegradationWorld();
        for(Entity entity : degradable.getAreaEntities()) {
            if(entity instanceof EntityPlayer) {
                world.playSound((EntityPlayer) entity, entity.posX, entity.posY, entity.posZ, SoundEvents.entity_blaze_ambient, SoundCategory.AMBIENT, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                world.playSound((EntityPlayer) entity, entity.posX, entity.posY, entity.posZ, SoundEvents.entity_enderdragon_flap, SoundCategory.AMBIENT, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            }
        }
    }
    
}
