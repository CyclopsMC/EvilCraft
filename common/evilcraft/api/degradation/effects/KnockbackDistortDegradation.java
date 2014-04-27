package evilcraft.api.degradation.effects;

import java.util.List;

import net.minecraft.entity.Entity;
import evilcraft.api.Coordinate;
import evilcraft.api.degradation.IDegradable;
import evilcraft.api.degradation.IDegradationEffect;
import evilcraft.items.MaceOfDistortion;

/**
 * An effect that will knockback the entities within the range of the degradable.
 * @author rubensworks
 *
 */
public class KnockbackDistortDegradation implements IDegradationEffect {

    private static final int MINIMUM_DEGRADATION = 3;
    private static final int POWER_LEVEL = 1;

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
        Coordinate center = degradable.getLocation();
        double x = center.x;
        double y = center.y;
        double z = center.z;
        for(Entity entity : entities) {
            MaceOfDistortion.distortEntity(
                    degradable.getWorld(),
                    null,
                    entity,
                    x, y, z,
                    (int) degradable.getDegradation() * 10,
                    POWER_LEVEL
            );
        }
    }
    
}
