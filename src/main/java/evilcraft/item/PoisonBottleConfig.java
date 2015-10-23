package evilcraft.item;

import evilcraft.Configs;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.fluid.Poison;
import evilcraft.fluid.PoisonConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

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
