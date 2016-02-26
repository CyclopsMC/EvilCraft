package org.cyclops.evilcraft.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderTileEntityEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;

/**
 * Config for the {@link EnvironmentalAccumulator}.
 * @author rubensworks
 *
 */
public class EnvironmentalAccumulatorConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
	public static EnvironmentalAccumulatorConfig _instance;
	
	/**
	 * The cooldown tick for accumulating the weather.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to cool down")
	public static int defaultTickCooldown = MinecraftHelpers.MINECRAFT_DAY / 20;
	
	/**
	 * The default number of ticks it takes to process
	 * an item.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, isCommandable = true, comment = "Sets the default amount of ticks the environmental accumulator takes to process an item.")
	public static int defaultProcessItemTickCount = 100;
	
	/**
	 * Default speed with which an item will move
	 * when being processed by an environmental accumulator.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, isCommandable = true, comment = "Sets the default default speed in increments per tick with which an item will move when being process by an environmental accumulator.")
	public static double defaultProcessItemSpeed = 0.3d / 20;
	
	/**
     * Make a new instance.
     */
	public EnvironmentalAccumulatorConfig() {
		super(
				EvilCraft._instance,
				true,
				"environmentalAccumulator",
				null,
				EnvironmentalAccumulator.class
		);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRegistered() {
        super.onRegistered();
	    getMod().getProxy().registerRenderer(TileEnvironmentalAccumulator.class, new RenderTileEntityEnvironmentalAccumulator());
	}
	
	@Override
	protected String getConfigPropertyPrefix() {
		return "envirAcc";
	}

}