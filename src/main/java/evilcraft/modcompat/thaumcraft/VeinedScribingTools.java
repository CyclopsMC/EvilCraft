package evilcraft.modcompat.thaumcraft;

import evilcraft.core.config.configurable.ConfigurableDamageIndicatedItemFluidContainer;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.fluid.Blood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.IScribeTools;

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
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new VeinedScribingTools(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VeinedScribingTools getInstance() {
        return _instance;
    }

    private VeinedScribingTools(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, CAPACITY, Blood.getInstance());
        this.canPickUp = false;
    }

    @Override
    public int getMaxDamage() {
        return CAPACITY;
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
            drain(itemStack, (damage * USAGE) - fluidStack.amount, true);
        }
    }

}
