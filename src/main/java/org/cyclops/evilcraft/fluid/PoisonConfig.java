package org.cyclops.evilcraft.fluid;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.SoundActions;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for poison.
 * @author rubensworks
 *
 */
public class PoisonConfig extends FluidConfig {

    public PoisonConfig() {
        super(
                EvilCraft._instance,
                "poison",
                fluidConfig -> getDefaultFluidProperties(EvilCraft._instance,
                        "block/poison",
                        builder -> builder
                                .density(1000)
                                .viscosity(1000)
                                .temperature(290)
                                .rarity(Rarity.UNCOMMON)
                                .descriptionId("block.evilcraft.poison")
                                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH))
                        .bucket(() -> RegistryEntries.ITEM_BUCKET_POISON)
                        .block(() -> RegistryEntries.BLOCK_POISON)
        );
    }

}
