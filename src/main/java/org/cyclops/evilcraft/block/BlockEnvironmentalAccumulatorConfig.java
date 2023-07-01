package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockEnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class BlockEnvironmentalAccumulatorConfig extends BlockConfig {

    @ConfigurableProperty(category = "machine", isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to cool down")
    public static int defaultTickCooldown = MinecraftHelpers.MINECRAFT_DAY / 20;

    @ConfigurableProperty(category = "machine", isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to process an item.")
    public static int defaultProcessItemTickCount = 100;

    @ConfigurableProperty(category = "machine", isCommandable = true, comment = "Sets the default default speed in increments per tick with which an item will move when being process by an environmental accumulator.")
    public static double defaultProcessItemSpeed = 0.3d / 20;

    public BlockEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                eConfig -> new BlockEnvironmentalAccumulator(Block.Properties.of()
                        .strength(50.0F, 6000000.0F)
                        .sound(SoundType.METAL)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    @Override
    protected String getConfigPropertyPrefix(ConfigurableProperty annotation) {
        return "envir_acc";
    }

}
