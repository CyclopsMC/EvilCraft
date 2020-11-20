package org.cyclops.evilcraft.fluid;


import net.minecraft.item.Rarity;
import net.minecraftforge.fluids.ForgeFlowingFluid;
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
                fluidConfig -> new ForgeFlowingFluid.Source(
                        getDefaultFluidProperties(EvilCraft._instance,
                                "block/poison",
                                builder -> builder
                                        .density(1000)
                                        .viscosity(1000)
                                        .temperature(290)
                                        .rarity(Rarity.UNCOMMON)
                                        .translationKey("block.evilcraft.poison"))
                                .bucket(() -> RegistryEntries.ITEM_BUCKET_POISON)
                                .block(() -> RegistryEntries.BLOCK_POISON))
        );
    }
    
}
