package org.cyclops.evilcraft.fluid;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.SoundActions;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for blood.
 * @author rubensworks
 *
 */
public class BloodConfig extends FluidConfig {

    public BloodConfig() {
        super(
                EvilCraft._instance,
                "blood",
                fluidConfig -> getDefaultFluidProperties(EvilCraft._instance,
                        "block/blood",
                        builder -> builder
                                .density(1500)
                                .viscosity(3000)
                                .temperature(309)
                                .rarity(Rarity.COMMON)
                                .descriptionId("block.evilcraft.blood")
                                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH))
                        .bucket(() -> RegistryEntries.ITEM_BUCKET_BLOOD)
                        .block(() -> RegistryEntries.BLOCK_BLOOD)
        );
    }

}
