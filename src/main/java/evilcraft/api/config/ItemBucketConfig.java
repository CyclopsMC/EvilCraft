package evilcraft.api.config;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.Recipes;
import evilcraft.api.BucketHandler;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.config.configurable.ConfigurableFluid;

/**
 * Config for buckets, extension of {@link ItemConfig}.
 * @author rubensworks
 * @see ExtendedConfig
 * @see ItemConfig
 */
public abstract class ItemBucketConfig extends ItemConfig {

    /**
     * Make a new instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public ItemBucketConfig(boolean enabled, String namedId, String comment, Class<? extends Item> element) {
        super(enabled, namedId, comment, element);
    }
    
    /**
     * Get the {@link ConfigurableFluid} this bucket can contain.
     * @return the fluid.
     */
    public abstract ConfigurableFluid getFluidInstance();
    /**
     * Get the {@link ConfigurableBlockFluidClassic} this bucket can place / pick up.
     * @return the fluid block.
     */
    public abstract ConfigurableBlockFluidClassic getFluidBlockInstance();
    
    @Override
    public void onRegistered() {
        Item item = (Item) this.getSubInstance();
        FluidStack fluidStack = FluidRegistry.getFluidStack(getFluidInstance().getName(), FluidContainerRegistry.BUCKET_VOLUME);
        FluidContainerRegistry.registerFluidContainer(
                fluidStack,
                new ItemStack(item),
                new ItemStack(Items.bucket)
        );
        BucketHandler.getInstance().buckets.put(getFluidBlockInstance(), item);
        Recipes.BUCKETS.put(item, fluidStack);
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
