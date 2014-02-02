package evilcraft.api.config;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import evilcraft.Recipes;
import evilcraft.api.BucketHandler;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.config.configurable.ConfigurableFluid;

public abstract class ItemBucketConfig extends ItemConfig {

    public ItemBucketConfig(int defaultId, String name, String namedId,
            String comment, Class<? extends Item> element) {
        super(defaultId, name, namedId, comment, element);
    }
    
    public abstract ConfigurableFluid getFluidInstance();
    public abstract ConfigurableBlockFluidClassic getFluidBlockInstance();
    
    public void onRegistered() {
        Item item = (Item) this.getSubInstance();
        FluidStack fluidStack = FluidRegistry.getFluidStack(getFluidInstance().getName(), FluidContainerRegistry.BUCKET_VOLUME);
        FluidContainerRegistry.registerFluidContainer(
                fluidStack,
                new ItemStack(item),
                new ItemStack(Item.bucketEmpty)
        );
        BucketHandler.getInstance().buckets.put(getFluidBlockInstance(), item);
        Recipes.BUCKETS.put(item, fluidStack);
    }

}
