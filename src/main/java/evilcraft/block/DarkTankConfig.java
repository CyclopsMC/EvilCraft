package evilcraft.block;

import net.minecraft.item.ItemBlock;
import evilcraft.client.render.tileentity.TileEntityDarkTankRenderer;
import evilcraft.core.config.BlockConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.item.ItemBlockNBT;
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
        return ItemBlockNBT.class;
    }
    
    @Override
    public void onRegistered() {
        if (MinecraftHelpers.isClientSide()) {
            ClientProxy.TILE_ENTITY_RENDERERS.put(TileDarkTank.class,
            		new TileEntityDarkTankRenderer());
            /*ClientProxy.ITEM_RENDERERS.put(Item.getItemFromBlock(BoxOfEternalClosure.getInstance()),
            		new RenderItemBoxOfEternalClosure(model, texture));*/
        }
    }

}
