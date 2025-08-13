package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class BlockEnvironmentalAccumulatorConfig extends BlockConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "machine", isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to cool down")
    public static int defaultTickCooldown = EvilCraft._instance.getModHelpers().getMinecraftHelpers().getDayLength() / 20;

    @ConfigurablePropertyCommon(category = "machine", isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to process an item.")
    public static int defaultProcessItemTickCount = 100;

    @ConfigurablePropertyCommon(category = "machine", isCommandable = true, comment = "Sets the default default speed in increments per tick with which an item will move when being process by an environmental accumulator.")
    public static double defaultProcessItemSpeed = 0.3d / 20;

    public BlockEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                (eConfig, properties) -> new BlockEnvironmentalAccumulator(properties
                        .strength(50.0F, 6000000.0F)
                        .sound(SoundType.METAL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    @Override
    public String getConfigPropertyPrefix(ConfigurablePropertyCommon annotation) {
        return "envir_acc";
    }

}
