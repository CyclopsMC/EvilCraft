package evilcraft.core.recipes;

import evilcraft.api.recipes.custom.IRecipeProperties;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.entities.tileentities.environmentalaccumulator.IEAProcessingFinishedEffect;
import lombok.*;

/**
 * Additional properties that are used to process {@link evilcraft.api.recipes.custom.IRecipe}S for the
 * {@link evilcraft.blocks.EnvironmentalAccumulator}.
 * @author immortaleeb
 */
public class EnvironmentalAccumulatorRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final double processingSpeed;
    private final int cooldownTime;
    @Getter
    private final IEAProcessingFinishedEffect finishedProcessingEffect;
    private final DurationRecipeProperties duration;

    public EnvironmentalAccumulatorRecipeProperties(int duration, int cooldownTime, double processingSpeed,
                                                    IEAProcessingFinishedEffect finishedProcessingEffect) {
        this.duration = new DurationRecipeProperties(duration);
        this.processingSpeed = processingSpeed;
        this.cooldownTime = cooldownTime;
        this.finishedProcessingEffect = finishedProcessingEffect;
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
}
