package evilcraft.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import evilcraft.client.render.block.RenderDarkTank;
import evilcraft.client.render.item.RenderItemDarkTank;
import evilcraft.client.render.tileentity.RenderTileEntityDarkTank;
import evilcraft.core.config.BlockConfig;
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
