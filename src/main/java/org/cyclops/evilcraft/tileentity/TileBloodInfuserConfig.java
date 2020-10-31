package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileBloodInfuser}.
 * @author rubensworks
 *
 */
public class TileBloodInfuserConfig extends TileEntityConfig<TileBloodInfuser> {

    public TileBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser",
                (eConfig) -> new TileEntityType<>(TileBloodInfuser::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_INFUSER), null)
        );
    }

}
