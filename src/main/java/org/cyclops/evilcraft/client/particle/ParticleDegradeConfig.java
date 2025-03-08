package org.cyclops.evilcraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ParticleDegrade}.
 * @author rubensworks
 */
public class ParticleDegradeConfig extends ParticleConfigCommon<SimpleParticleType, IModBase> {

    public ParticleDegradeConfig() {
        super(EvilCraft._instance, "degrade", eConfig -> new SimpleParticleType(false));
    }

    @Override
    public ParticleConfigComponentClient<SimpleParticleType, IModBase> getClientComponent() {
        return new ParticleDegradeConfigClientComponent();
    }
}
