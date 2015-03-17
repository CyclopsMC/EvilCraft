package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.fluid.Blood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Config for the {@link CreativeBloodDrop}.
 * @author rubensworks
 *
 */
public class CreativeBloodDropConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static CreativeBloodDropConfig _instance;

    /**
     * Make a new instance.
     */
    public CreativeBloodDropConfig() {
        super(
        	true,
            "creativeBloodDrop",
            null,
            CreativeBloodDrop.class
        );
    }
    
    @Override
    public void onRegistered() {
        ItemStack itemStack = new ItemStack(CreativeBloodDrop.getInstance(), 1);
        FluidContainerRegistry.registerFluidContainer(
                FluidRegistry.getFluidStack(Blood.getInstance().getName(), CreativeBloodDrop.getInstance().getCapacity(itemStack)),
                itemStack
        );
    }
    
}
