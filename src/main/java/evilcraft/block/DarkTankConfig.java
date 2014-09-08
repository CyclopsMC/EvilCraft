package evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import evilcraft.client.render.block.RenderDarkTank;
import evilcraft.client.render.item.RenderItemDarkTank;
import evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.configurable.ConfigurableProperty;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemBlockFluidContainer;
import evilcraft.proxy.ClientProxy;
import evilcraft.tileentity.TileDarkTank;

/**
 * Config for the {@link DarkTank}.
 * @author rubensworks
 *
 */
public class DarkTankConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static DarkTankConfig _instance;
    
    /**
	 * The maximum tank size possible by combining tanks.
	 */
	@ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The maximum tank size possible by combining tanks. (Make sure that you do not cross the max int size.)")
	public static int maxTankSize = 4096000;

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
        	ClientProxy.BLOCK_RENDERERS.add(new RenderDarkTank());
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileDarkTank.class,
            		new RenderTileEntityDarkTank());
            ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(DarkTank.getInstance()),
            		new RenderItemDarkTank());
        }
    }

}
