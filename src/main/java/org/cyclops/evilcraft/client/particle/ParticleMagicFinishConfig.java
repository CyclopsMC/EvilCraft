package org.cyclops.evilcraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ParticleMagicFinish}.
 * @author rubensworks
 */
public class ParticleMagicFinishConfig extends ParticleConfigCommon<SimpleParticleType, IModBase> {

    public ParticleMagicFinishConfig() {
        super(EvilCraft._instance, "magic_finish", eConfig -> new SimpleParticleType(false));
    }

    @Override
    public ParticleConfigComponentClient<SimpleParticleType, IModBase> getClientComponent() {
        return new ParticleMagicFinishConfigClientComponent();
    }
}
