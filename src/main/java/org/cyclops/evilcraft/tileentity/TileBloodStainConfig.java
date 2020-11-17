package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the {@link TileBloodStain}.
 * @author rubensworks
 *
 */
public class TileBloodStainConfig extends TileEntityConfig<TileBloodStain> {

    public TileBloodStainConfig() {
        super(
                EvilCraft._instance,
                "blood_stain",
                (eConfig) -> new TileEntityType<>(TileBloodStain::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BLOOD_STAIN), null)
        );
    }

}
