package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.fluid.Poison;
import org.cyclops.evilcraft.fluid.PoisonConfig;

/**
 * Config for the {@link PoisonBottle}.
 * @author rubensworks
 *
 */
public class PoisonBottleConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static PoisonBottleConfig _instance;

    /**
     * Make a new instance.
     */
    public PoisonBottleConfig() {
        super(
                EvilCraft._instance,
        	true,
            "poisonBottle",
            null,
            PoisonBottle.class
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(Configs.isEnabled(PoisonConfig.class)) {
            FluidContainerRegistry.registerFluidContainer(Poison.getInstance(), new ItemStack(PoisonBottle.getInstance()), FluidContainerRegistry.EMPTY_BOTTLE);
        }
    }
}
