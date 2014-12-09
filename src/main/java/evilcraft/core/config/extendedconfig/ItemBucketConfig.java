package evilcraft.core.config.extendedconfig;

import evilcraft.Recipes;
import evilcraft.core.BucketHandler;
import evilcraft.core.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.core.config.configurable.ConfigurableFluid;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
    
    @Override
	public String getUnlocalizedName() {
		return "items." + getNamedId();
	}
    
    /**
     * Get the {@link ConfigurableFluid} this bucket can contain.
     * @return the fluid.
     */
    public abstract Fluid getFluidInstance();
    /**
     * Get the {@link ConfigurableBlockFluidClassic} this bucket can place / pick up.
     * @return the fluid block.
     */
    public abstract Block getFluidBlockInstance();
    
    @Override
    public void onRegistered() {
        Item item = (Item) this.getSubInstance();
        if(getFluidInstance() != null) {
            FluidStack fluidStack = FluidRegistry.getFluidStack(getFluidInstance().getName(), FluidContainerRegistry.BUCKET_VOLUME);
            FluidContainerRegistry.registerFluidContainer(
                    fluidStack,
                    new ItemStack(item),
                    new ItemStack(item.getContainerItem())
            );
            Recipes.BUCKETS.put(item, fluidStack);
        }
        if(getFluidBlockInstance() != Blocks.air) {
            BucketHandler.getInstance().buckets.put(getFluidBlockInstance(), item);
        }
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
