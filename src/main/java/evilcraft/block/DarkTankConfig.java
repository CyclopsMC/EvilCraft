package evilcraft.block;

import evilcraft.client.render.block.RenderDarkTank;
import evilcraft.client.render.item.RenderItemDarkTank;
import evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.BlockContainerConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileDarkTank;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

/**
 * Config for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class DarkTankConfig extends BlockContainerConfig {
    
    /**
     * The unique instance.
     */
    public static DarkTankConfig _instance;
    
    /**
	 * The maximum tank size possible by combining tanks.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The maximum tank size possible by combining tanks. (Make sure that you do not cross the max int size.)")
	public static int maxTankSize = 65536000;
	/**
	 * The maximum tank size visible in the creative tabs.
	 */
	@ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "The maximum tank size visible in the creative tabs. (Make sure that you do not cross the max int size.)")
	public static int maxTankCreativeSize = 4096000;
    /**
     * If creative versions for all fluids should be added to the creative tab.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MACHINE, comment = "If creative versions for all fluids should be added to the creative tab.")
    public static boolean creativeTabFluids = true;

    /**
     * Make a new instance.
     */
    public DarkTankConfig() {
        super(
        	true,
            "darkTank",
            null,
            DarkTank.class
        );
    }
    
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockFluidContainer.class;
    }
    
    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
            // TODO
        	//ClientProxy.BLOCK_RENDERERS.add(new RenderDarkTank());
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileDarkTank.class,
            		new RenderTileEntityDarkTank());
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(DarkTank.getInstance()),
            		new RenderItemDarkTank());
        }
    }

}
