package org.cyclops.evilcraft.modcompat.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.fluid.Blood;
import thaumcraft.api.items.IScribeTools;

/**
 * Scribing tools that run on Blood.
 * Texture is based on the one from Thaumcraft.
 * @author rubensworks
 *
 */
public class VeinedScribingTools extends ConfigurableDamageIndicatedItemFluidContainer implements IScribeTools {

    private static final int CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 2;
    private static final int USAGE = 10;

    private static VeinedScribingTools _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VeinedScribingTools getInstance() {
        return _instance;
    }

    public VeinedScribingTools(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CAPACITY, Blood.getInstance());
        setMaxDamage(CAPACITY / USAGE);
        this.canPickUp = false;
    }

    @Override
    public int getDamage(ItemStack itemStack) {
        FluidStack fluidStack = getFluid(itemStack);
        if(fluidStack == null) return 0;
        return (CAPACITY - fluidStack.amount) / USAGE;
    }

    @Override
    public void setDamage(ItemStack itemStack, int damage) {
        FluidStack fluidStack = getFluid(itemStack);
        if(fluidStack != null) {
            drain(itemStack, (damage - getDamage(itemStack)) * USAGE, true);
        }
    }

}
