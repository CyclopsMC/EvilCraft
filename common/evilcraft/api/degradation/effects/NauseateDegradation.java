package evilcraft.api.degradation.effects;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.IDegradationEffect;

/**
 * Degradation effect that will nauseate entities in the degradation area.
 * @author rubensworks
 *
 */
public class NauseateDegradation implements IDegradationEffect {
    
    private static final int MINIMUM_DEGRADATION = 1;
    private static final int NAUSEA_DURATION_MULTIPLIER = 20 * 4;

    @Override
    public boolean canRun(IDegradable degradable) {
        return degradable.getDegradation() >= MINIMUM_DEGRADATION;
    }

    @Override
    public void runClientSide(IDegradable degradable) {
        
    }

    @Override
    public void runServerSide(IDegradable degradable) {
        List<Entity> entities = degradable.getAreaEntities();
        for(Entity entity : entities) {
            if(entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(
                        new PotionEffect(
                                Potion.confusion.id,
                                (int) degradable.getDegradation() * NAUSEA_DURATION_MULTIPLIER, 1)
                        );
            }
        }
    }

}
