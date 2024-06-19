package org.cyclops.evilcraft.core.fluid;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.logging.log4j.Level;
import org.cyclops.evilcraft.EvilCraft;

import java.util.Map;

/**
 * An implicit converter for changing fluid types with a conversion rate.
 * @author rubensworks
 *
 */
public class ImplicitFluidConverter {

    private static final String DELIMITER = ":";

    private Fluid target;
    private Map<Fluid, Double> converters = Maps.newHashMap();

    /**
     * Make a new instance.
     * @param target The fluid to convert to.
     */
    public ImplicitFluidConverter(Fluid target) {
        setTarget(target);
    }

    /**
     * Set a new target, this will not remove the old target from the possible converters.
     * @param target The target fluid.
     */
    public void setTarget(Fluid target) {
        this.target = target;
        if(target != null) {
            converters.put(target, 1.0D); // Also accept the target.
        }
    }

    /**
     * @return The target fluid.
     */
    public Fluid getTarget() {
        return target;
    }

    /**
     * Add a fluid converter.
     * @param fluid The fluid to convert to.
     * @param rate The rate of conversion that will be multiplied with every conversion.
     */
    public void addConverter(Fluid fluid, double rate) {
        if(fluid != null) {
            EvilCraft.clog("Register fluid conversion for " + BuiltInRegistries.FLUID.getKey(fluid) + " with ratio " + rate);
            converters.put(fluid, rate);
        }
    }

    /**
     * If the given fluid can be converter.
     * @param fluid The fluid to convert.
     * @return If it can be converted.
     */
    public boolean canConvert(Fluid fluid) {
        return converters.containsKey(fluid);
    }

    /**
     * Convert the given fluid to the target fluid.
     * @param fluid The fluid stack to convert from.
     * @return The converted fluid stack.
     */
    public FluidStack convert(FluidStack fluid) {
        if(!fluid.isEmpty() && canConvert(fluid.getFluid())) {
            return new FluidStack(target, (int) Math.floor(fluid.getAmount()
                    * converters.get(fluid.getFluid())));
        }
        return FluidStack.EMPTY;
    }

    /**
     * Convert the given fluid from target fluid to the given fluid.
     * @param target The fluid to convert to.
     * @param fluid The fluid stack to convert, it must be of the same fluid type of the main target of this converter instance.
     * @return The converted fluid stack.
     */
    public FluidStack convertReverse(Fluid target, FluidStack fluid) {
        if(canConvert(target) && !fluid.isEmpty() && fluid.getFluid() == this.target) {
            FluidStack ret = fluid.copy();
            return new FluidStack(target, (int) Math.floor(ret.getAmount()
                    / converters.get(target)));
        }
        return FluidStack.EMPTY;
    }

    /**
     * The conversion ratio to the given target fluid.
     * @param target The target fluid.
     * @return The conversion ratio.
     */
    public double getRatio(Fluid target) {
        return converters.get(target);
    }

    /**
     * Register the converters config from the given string array.
     * @param config The config where each element is in the form 'fluidname:ratio'.
     */
    public void registerFromConfig(String[] config) {
        for(String line : config) {
            String[] split = line.split(DELIMITER);
            if(split.length != 2) {
                throw new IllegalArgumentException("Invalid line '" + line + "' found for "
                        + "a fluid converter config.");
            }
            Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(split[0]));
            if(fluid == null) {
                EvilCraft.clog("Could not find a fluid by name '" + split[0] + "' for "
                        + "a fluid converter config.", Level.WARN);
            }
            double ratio = 1.0D;
            try {
                ratio = Double.parseDouble(split[1]);
            } catch (NumberFormatException e) {
                EvilCraft.clog("Invalid ratio '" + split[1] + "' in "
                        + "a fluid converter config, using 1.0.", Level.ERROR);
            }
            addConverter(fluid, ratio);
        }
    }


}
