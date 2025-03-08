package org.cyclops.evilcraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ParticleBloodBubble}.
 * @author rubensworks
 */
public class ParticleBloodBubbleConfig extends ParticleConfigCommon<SimpleParticleType, IModBase> {

    public ParticleBloodBubbleConfig() {
        super(EvilCraft._instance, "blood_bubble", eConfig -> new SimpleParticleType(false));
    }

    @Override
    public ParticleConfigComponentClient<SimpleParticleType, IModBase> getClientComponent() {
        return new ParticleBloodBubbleConfigComponentClient();
    }
}
