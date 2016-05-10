package org.cyclops.evilcraft.core.recipe.custom;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.block.EnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.tileentity.environmentalaccumulator.IEAProcessingFinishedEffect;

/**
 * Additional properties that are used to process {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe}S for the
 * {@link EnvironmentalAccumulator}.
 * @author immortaleeb
 */
public class EnvironmentalAccumulatorRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final double processingSpeed;
    private final int cooldownTime;
    @Getter
    private final IEAProcessingFinishedEffect finishedProcessingEffect;
    private final DurationRecipeProperties duration;
    @Getter
    private final IEAResultOverride resultOverride;

    public EnvironmentalAccumulatorRecipeProperties(int duration, int cooldownTime, double processingSpeed,
                                                    IEAProcessingFinishedEffect finishedProcessingEffect, IEAResultOverride resultOverride) {
        this.duration = new DurationRecipeProperties(duration);
        this.processingSpeed = processingSpeed;
        this.cooldownTime = cooldownTime;
        this.finishedProcessingEffect = finishedProcessingEffect;
        this.resultOverride = resultOverride;
    }

    public EnvironmentalAccumulatorRecipeProperties(int duration, int cooldownTime, double processingSpeed,
                                                    IEAProcessingFinishedEffect finishedProcessingEffect) {
        this(duration, cooldownTime, processingSpeed, finishedProcessingEffect, new IEAResultOverride() {
            @Override
            public ItemStack getResult(IBlockAccess world, BlockPos pos, ItemStack originalResult) {
                return originalResult;
            }
        });
    }

    public EnvironmentalAccumulatorRecipeProperties(int duration, int cooldownTime, double processingSpeed) {
        this(duration, cooldownTime, processingSpeed, null);
    }

    public EnvironmentalAccumulatorRecipeProperties(int duration, int cooldownTime) {
        this(duration, cooldownTime, -1.0);
    }

    public EnvironmentalAccumulatorRecipeProperties(int duration) {
        this(duration, -1);
    }

    public EnvironmentalAccumulatorRecipeProperties() {
        this(-1);
    }

    /**
     * @return Returns the processing speed of the associated recipe.
     */
    public double getProcessingSpeed() {
        // Note: we need to do this because defaultProcessItemSpeed is set AFTER the recipes are created
        if (processingSpeed < 0)
            return EnvironmentalAccumulatorConfig.defaultProcessItemSpeed;

        return processingSpeed;
    }

    /**
     * @return Returns the cooldown time (in ticks) of the associated recipe.
     */
    public int getCooldownTime() {
        // Note: we need to do this because defaultProcessItemTickCount is set AFTER the recipes are created
        if (cooldownTime < 0)
            return EnvironmentalAccumulatorConfig.defaultTickCooldown;

        return cooldownTime;
    }

    @Override
    public int getDuration() {
        // Note: we need to do this because defaultProcessItemTickCount is set AFTER the recipes are created
        if (duration.getDuration() < 0)
            return EnvironmentalAccumulatorConfig.defaultProcessItemTickCount;

        return duration.getDuration();
    }

    public interface IEAResultOverride {

        public ItemStack getResult(IBlockAccess world, BlockPos pos, ItemStack originalResult);

    }

}
