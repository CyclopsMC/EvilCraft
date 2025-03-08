package org.cyclops.evilcraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link ParticleBloodSplash}.
 * @author rubensworks
 */
public class ParticleBloodSplashConfig extends ParticleConfigCommon<SimpleParticleType, IModBase> {

    public ParticleBloodSplashConfig() {
        super(EvilCraft._instance, "blood_splash", eConfig -> new SimpleParticleType(false));
    }

    @Override
    public ParticleConfigComponentClient<SimpleParticleType, IModBase> getClientComponent() {
        return new ParticleBloodSplashConfigComponentClient();
    }
}
