package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.client.render.tileentity.RenderItemStackTileEntityDarkTank;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

/**
 * Config for the {@link BlockDarkTank}.
 * @author rubensworks
 *
 */
public class BlockDarkTankConfig extends BlockConfig {

	@ConfigurableProperty(category = "machine", comment = "The maximum tank size possible by combining tanks. (Make sure that you do not cross the max int size.)")
	public static int maxTankSize = 65536000;

	@ConfigurableProperty(category = "machine", comment = "The maximum tank size visible in the creative tabs. (Make sure that you do not cross the max int size.)")
	public static int maxTankCreativeSize = 4096000;

    @ConfigurableProperty(category = "machine", comment = "If creative versions for all fluids should be added to the creative tab.")
    public static boolean creativeTabFluids = true;

    @ConfigurableProperty(category = "machine", comment = "If the fluid should be rendered statically. Fluids won't be shown fluently, but more efficiently.tab.", requiresMcRestart = true)
    public static boolean staticBlockRendering = false;

    @ConfigurableProperty(category = "item", comment = "If held buckets should be autofilled when enabled.", isCommandable = true)
    public static boolean autoFillBuckets = false;

    public BlockDarkTankConfig() {
        super(
                EvilCraft._instance,
            "dark_tank",
                eConfig -> new BlockDarkTank(Block.Properties.create(Material.GLASS)
                        .hardnessAndResistance(0.5F)
                        .sound(SoundType.GLASS)),
                (eConfig, block) -> new ItemBlockFluidContainer(block, (new Item.Properties())
                        .group(EvilCraft._instance.getDefaultItemGroup())
                        .setISTER(() -> () -> new RenderItemStackTileEntityDarkTank()))
        );
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getCutout());
    }

}
